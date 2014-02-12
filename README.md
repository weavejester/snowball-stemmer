# Snowball-Stemmer

Provides a simple wrapper around the tartarus.org Snowball stemmer.

## Installation

To install, add the following to your project `:dependencies`:

    [snowball-stemmer "0.1.0"]

## Usage

```clojure
user=> (require '[stemmer.snowball :as sn])
user=> ((sn/stemmer :english) "mice")
"mice"
user=> ((sn/stemmer :english) "mices")
"mice"
user=> ((sn/stemmer :english) "turtles")
"turtl"
user=> ((sn/stemmer :english) "turtle")
"turtl"
```

## License

Copyright © 2014 James Reeves

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
