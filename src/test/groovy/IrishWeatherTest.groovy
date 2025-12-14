package net.ebdon.irishweather

import groovy.test.GroovyTestCase
import groovy.mock.interceptor.MockFor
import groovy.ant.AntBuilder
import org.apache.tools.ant.Project

/**
 * Unit tests for net.ebdon.irishweather.IrishWeather.
 *
 * All external interactions (Ant, Project) are mocked so tests are hermetic and
 * do not perform network or filesystem I/O.
 */
@Newify(MockFor)
@groovy.util.logging.Log4j2('logger')
class IrishWeatherTest extends GroovyTestCase {

  /**
   * Verify cleanTitle removes the leading episode number and label,
   * returning only the timestamp and date string used to build filenames.
   */
  void testCleanTitle() {
    IrishWeather iw = new IrishWeather()
    String rawTitle = '123: Ireland\'s Weather 6am Monday 1 January 2020'
    String cleaned = iw.cleanTitle(rawTitle)
    assert cleaned == '6am Monday 1 January 2020'
  }

  /**
   * Verify buildMp3FileNames produces expected filenames for an AM
   * timestamp. The expected format is 'YYYY-MM-DD_HH00_DDD.mp3'.
   */
  void testBuildMp3FileNamesAm() {
    IrishWeather iw = new IrishWeather()
    iw.buildMp3FileNames('6am Monday 1 January 2020')
    assert iw.trimmedFileName == '2020-01-01_0600_MON.mp3'
    assert iw.mp3FileName == 'raw_2020-01-01_0600_MON.mp3'
  }

  /**
   * Verify buildMp3FileNames produces expected filenames for a PM
   * timestamp (hour converted to 24-hour clock).
   */
  void testBuildMp3FileNamesPm() {
    IrishWeather iw = new IrishWeather()
    iw.buildMp3FileNames('6pm Tuesday 2 February 2021')
    assert iw.trimmedFileName == '2021-02-02_1800_TUE.mp3'
    assert iw.mp3FileName == 'raw_2021-02-02_1800_TUE.mp3'
  }

  /**
   * Verify trimAudio invokes AntBuilder.exec() and that the mocked Ant Project
   * provides the expected properties used by trimAudio for output,
   * error and result values.
   */
  void testTrimAudioSetsAntProjectProperties() {
    MockFor antMock     = MockFor(AntBuilder)
    MockFor projectMock = MockFor(Project)
    projectMock.
      demand.getProperties {
        [
          trimCmdResult: '1',
          trimCmdOut   : 'out',
          trimCmdError : 'err',
        ]
      }

    antMock.demand.with {
      exec { Map attrs, Closure closure ->
        assert closure != null
      }
      getProject { new Project() }
    }

    projectMock.use {
      antMock.use {
        new IrishWeather().with {
          mp3FileName     = 'raw_test.mp3'
          trimmedFileName = 'test.mp3'
          trimAudio()
        }
      }
    }
  }
}
