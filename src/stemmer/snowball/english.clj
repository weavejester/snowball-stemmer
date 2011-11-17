(ns stemmer.snowball.english
  "Implementation of the Snowball stemming algorithm for the English language.
  See: http://snowball.tartarus.org/algorithms/english/stemmer.html"
  (:use [stemmer.util :only (replace-longest)])
  (:require [clojure.string :as str]))

(def vowel #"[aeiouy]")

(def non-vowel #"[^aeiouy]")

(def double-letter #"(?:bb|dd|ff|gg|mm|nn|pp|rr|tt)")

(defn pattern [& ss]
  (re-pattern (apply str ss)))

(defn ends-with? [s re]
  (re-find (pattern "(?:" re ")$") s))

(defn in? [m1 m2]
  (and (.find m1 0)
       (.find m2 0)
       (<= (.start m1) (.start m2))
       (>= (.end m1) (.end m2))))

(defn r1 [word]
  (-> (pattern "(?<=" vowel non-vowel ").*$")
      (re-matcher word)))

(defn in-r1? [word re]
  (in? (r1 word) (re-matcher re word)))

(def short-syllable
  (pattern
   "(?:" non-vowel vowel #"[^aeiouwxY]"
   "|^" vowel non-vowel ")"))

(defn short? [word]
  (and (ends-with? word short-syllable)
       (str/blank? (re-find (r1 word)))))

(defn step-0 [word]
  (str/replace word #"('s'|'s|')$" ""))

(defn step-1a [word]
  (replace-longest word
    #"sses$"  "ss"
    #"ied$"   "ies"
    #"s$"     #(if (re-find (pattern vowel ".*..$") word) "" %)
    #"us|ss"  identity))

(defn step-1b [word]
  (replace-longest word
    #"eed(ly)?$"
    #(if (in-r1? word #"eed(ly)?$") "ee" %)
    #"(.)(ed|ing)(ly)?$"
    (fn [[_ last-letter]]
      (let [[_ preceed suffix] (re-find #"^(.*)((?:ed|ing)(?:ly)?)$" word)]
        (if-not (re-find vowel preceed)
          suffix
          (cond
           (ends-with? preceed #"at|bl|iz") (str last-letter "e")
           (ends-with? preceed double-letter) ""
           (short? preceed) (str last-letter "e")
           :else last-letter))))))
