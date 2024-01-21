package net.ebdon.irishweather

import groovy.ant.AntBuilder

class IrishWeather {
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

    final String mp3FileName = buildMp3FileName( cleanTitle( title ))
    println mp3Url
    println title
    println mp3FileName
    ant.get( src: mp3Url, dest: mp3FileName, usetimestamp: true, quiet: true )
  }

  final String buildMp3FileName( final String title ) {
    def fnBits        = title.split()
    String year       = fnBits[-1]
    int month         = getMonthNumber( fnBits[-2] )
    int day           = Integer.parseInt(fnBits[-3])
    String dayName    = fnBits[1].toUpperCase()[0..2]
    int hour          = Integer.parseInt(fnBits[0] - 'am' - 'pm' )

    if ( fnBits[0].toLowerCase().contains('pm') && hour != 12 ) {
      hour += 12
    }

    String.format( '%4s-%02d-%02d_%02d00_%s.mp3',
        year, month, day, hour, dayName )
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

  static void main(String[] args) {
    new IrishWeather().run()
  }
}