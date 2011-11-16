(ns stemmer.snowball.english
  "Implementation of the Snowball stemming algorithm for the English language.
  See: http://snowball.tartarus.org/algorithms/english/stemmer.html"
  (:require [clojure.string :as str]))

(defn step-0 [word]
  (str/replace word #"('s'|'s|')$" ""))
