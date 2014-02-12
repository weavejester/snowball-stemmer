# snowball-stemmer

Provides a simple wrapper around the tartarus.org Snowball stemmer.  

## Usage

After you install it to your maven repo, you can 
add it to your `project.clj`

    [snowball-stemmer "0.1.0"]

and then, you can use it in the `lein repl`


    user=> (require '[stemmer.snowball :as sn])
    user=> ((sn/stemmer :english) "mice")
    "mice"
    user=> ((sn/stemmer :english) "mices")
    "mice"
    user=> ((sn/stemmer :english) "turtles")
    "turtl"
    user=> ((sn/stemmer :english) "turtle")
    "turtl"

## License

Distributed under the Eclipse Public License, the same as Clojure.
