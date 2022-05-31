# Snowball-Stemmer

Provides a simple wrapper around the tartarus.org Snowball stemmer.

## Getting started

Add the necessary dependency

### `deps.edn`

    snowball-stemmer/snowball-stemmer {:mvn/version "0.2.0"}

## Building from source

Build locally to .m2 repository

    clj -T:build clean
    clj -T:build jar
    clj -T:build install

## Usage

```clojure
user=> (require '[stemmer.snowball :as snowball])
nil
user=> (def stemmer (snowball/stemmer :english))
#'user/stemmer
user=> (stemmer "probable")
"probabl"
user=> (stemmer "probably")
"probabl"
user=> (stemmer "loved")
"love"
user=> (stemmer "lovely")
"love"
user=> (stemmer "turtles")
"turtl"
user=> (stemmer "turtle")
"turtl"
```

## License

Distributed under the BSD license.
