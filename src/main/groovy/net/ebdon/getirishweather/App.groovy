package net.ebdon.irishweather

import groovy.ant.AntBuilder

@groovy.util.logging.Log4j2
class IrishWeather {

  final String q = '\"'
  final AntBuilder ant = new AntBuilder()
  String mp3FileName
  String trimmedFileName

  String run() {
    final String rssFileName = 'rss.xml'
    final String rssUrl = 'https://audioboom.com/channels/5003380.rss'
    AntBuilder ant = new AntBuilder()
    ant.get( src: rssUrl, dest: rssFileName, usetimestamp: true, quiet: true )
    def rss = new XmlParser().parse(
      new File( rssFileName )
    )
    final String mp3Url   = rss.channel.item[0].enclosure.@url[0]
    final String title    = rss.channel.item[0].title.text()

    // writePubDatesFile( rss )

    buildMp3FileNames( cleanTitle( title ))
    log.info mp3Url
    log.info title
    log.info mp3FileName
    ant.get( src: mp3Url, dest: mp3FileName, usetimestamp: true, quiet: true )
    if ( new File(mp3FileName).exists() ) {
      trimAudio()
      makeAvailable()
    } else {
      log.error "Couldn't download $mp3FileName"
    }
  }

  void makeAvailable() {
    ant.copy(
      file     : trimmedFileName,
      tofile   : 'weather.mp3',
      overwrite: true
    )
  }

  void buildMp3FileNames( final String title ) {
    def fnBits        = title.split()
    String year       = fnBits[-1]
    int month         = getMonthNumber( fnBits[-2] )
    int day           = Integer.parseInt(fnBits[-3])
    String dayName    = fnBits[1].toUpperCase()[0..2]
    int hour          = Integer.parseInt(fnBits[0] - 'am' - 'pm' )

    if ( fnBits[0].toLowerCase().contains('pm') && hour != 12 ) {
      hour += 12
    }

    trimmedFileName = String.format( '%4s-%02d-%02d_%02d00_%s.mp3',
        year, month, day, hour, dayName )
    mp3FileName = 'raw_' + trimmedFileName
  }

  private final int getMonthNumber(String monthName) {
    return java.time.Month.valueOf(monthName.toUpperCase()).getValue()
  }

  final String cleanTitle( String title ) {
    final String mp3FileNameRegex = /([0-9]+): Ireland's Weather /
    title.replaceAll( mp3FileNameRegex, '' )
  }

  void writePubDatesFile( def final rss ) {
    File pubDates = new File('pubDates.txt')
    rss.channel.item.each { 
      final String title = it.title.text()
      pubDates << String.format( '%s -- %s%n',
        it.pubDate.text(), title
      )
    }
  }

  void trimAudio() {
    final String logLevel = '-loglevel error'
    final String input = "-i $q$mp3FileName$q"

    final String trimTrackArgs = 
      'areverse,atrim=start=0.2,silenceremove=start_periods=1:start_silence=0.1:start_threshold=0.02'
    final String ffmpedArgs = "$logLevel -af $q$trimTrackArgs,$trimTrackArgs$q"
    final String argsLine = "-y $input $ffmpedArgs $q$trimmedFileName$q"
    log.info "argsLine: $argsLine"
    ant.exec (
      dir               : '.',
      executable        : 'ffmpeg',
      outputproperty    : 'trimCmdOut',
      errorproperty     : 'trimCmdError',
      resultproperty    : 'trimCmdResult'
    ) {
      arg( line: argsLine )
    }
    final int execRes       = ant.project.properties.trimCmdResult.toInteger()
    final String execOut    = ant.project.properties.trimCmdOut
    final String execErr    = ant.project.properties.trimCmdError
    log.info "trimAudio execOut = $execOut"
    log.info "trimAudio execErr = $execErr"
    log.info "trimAudio execRes = $execRes"

    if ( !execErr.empty ) {
      log.error 'Could not trim audio'
      log.error execErr
      log.warn "out: $execOut"
      log.warn "result: $execRes"
    }
  }

  static void main(String[] args) {
    new IrishWeather().run()
  }
}
