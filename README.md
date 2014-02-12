# Snowball-Stemmer

Provides a simple wrapper around the tartarus.org Snowball stemmer.

## Installation

To install, add the following to your project `:dependencies`:

    [snowball-stemmer "0.1.0"]

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
