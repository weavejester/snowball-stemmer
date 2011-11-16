(ns stemmer.snowball.english
  "Implementation of the Snowball stemming algorithm for the English language.
  See: http://snowball.tartarus.org/algorithms/english/stemmer.html"
  (:use [stemmer.util :only (replace-longest)])
  (:require [clojure.string :as str]))

(defn step-0 [word]
  (str/replace word #"('s'|'s|')$" ""))

(defn step-1a [word]
  (replace-longest word
    #"sses$"  "ss"
    #"ied$"   "ies"
    #"s$"     #(if (re-find #"[aeiouy].*..$" word) "" %)
    #"us|ss"  identity))
