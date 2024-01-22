# GetIrishWeather

This app downloads the weather forecast podcasts, in English, as they becomes
available.

## Using the app

1. Install a compatible version of Java, see the prerequisites section.
1. Unzip or untar the distribution into a new folder.
2. Open a command window.
3. cd to the folder you used for step 1.
4. type the command `GetIrishWeather\bin\GetIrishWeather`
5. The latest weather forecast will be downloaded as an mp3 into the current
folder.

## Problem solving

1. Make sure your're using a compatible version of Java, see the prerequisites
section for details. Use the command `java --version` to determine which version
you're using.
2. The app creates a log folder. Check the log files, in that folder, for errors.
3. If you still can't get it to work then please [log
an issue][issue].

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
