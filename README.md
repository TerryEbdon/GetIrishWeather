# GetIrishWeather

This app downloads the weather forecast podcasts, in English, as they becomes
available.

## Background

The Weather Forecast from Met Éireann is under a creative commons by attribution
licence. They release an audio file, about three minutes long, three times a day
[here](https://audioboom.com/channels/5003380-weather-forecast-from-met-eireann).
There’s a download link for the MP3, bottom-right of the page for each forecast.
The forecasts are issued at approximately 6am, 1pm and 6pm.

## MP3 availability

The audio files are often released up to 10 minutes before the due time.
Sometimes they're an hour late. Occasionally a forecast can be skipped or
updated. On one occasion I've seen the Irish language forecast uploaded instead
of the English language version.

## Prerequisites

Currently this app requires Java 17 or higher. If you need it to work with a
different version then either build it from source or [log an issue][issue] and
I'll take a look.

This `GetIrishWeather` should work on any system with a suitable version of Java
and am internet connection. If it doesn't work on your system then please [log
an issue][issue].

[issue]: https://github.com/TerryEbdon/GetIrishWeather/issues

## Building from source

1. Make sure you have a suitable version of Java installed, and on the path.
2. Download or clone the repository.
3. From a command prompt issue the command: `gradlew build`.
