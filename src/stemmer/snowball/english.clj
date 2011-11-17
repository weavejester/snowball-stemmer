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

(defn group [matcher n]
  (if (.find matcher)
    {:start  (.start matcher n)
     :end    (.end matcher n)
     :string (.group matcher n)}))

(defn in-group? [group matcher]
  (and group
       (.find matcher 0)
       (<= (:start group) (.start matcher))
       (>= (:end group)   (.end matcher))))

(defn r1 [word]
  (-> (pattern vowel non-vowel "(.*)$")
      (re-matcher word)
      (group 1)))

(defn r2 [word]
  (-> (pattern vowel non-vowel ".*?" vowel non-vowel "(.*)$")
      (re-matcher word)
      (group 1)))

(defn in-r1? [word re]
  (in-group? (r1 word) (re-matcher re word)))

(defn in-r2? [word re]
  (in-group? (r2 word) (re-matcher re word)))

(def short-syllable
  (pattern
   "(?:" non-vowel vowel #"[^aeiouwxY]"
   "|^" vowel non-vowel ")"))

(defn short? [word]
  (and (ends-with? word short-syllable)
       (str/blank? (:string (r1 word)))))

(defn replace-longest-if [pred word & rules]
  (apply replace-longest word
    (mapcat
     (fn [[re replacement]]
       (if (fn? replacement)
         [re #(if (pred word re) (replacement %) %)]
         [re #(if (pred word re) replacement %)]))
     (partition 2 rules))))

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

(defn step-1c [word]
  (str/replace word (pattern "(?<=^.+" non-vowel ")[yY]$") "i"))

(defn step-2 [word]
  (replace-longest-if in-r1? word
    #"tional$"              "tion"
    #"enci$"                "ence"
    #"anci$"                "ance"
    #"abli$"                "able"
    #"entli$"               "ent"
    #"iz(er|ation)$"        "ize"
    #"at(ion|ional|or)$"    "ate"
    #"al(ism|iti|li)$"      "al"
    #"fulness$"             "ful"
    #"ous(li|ness)$"        "ous"
    #"iv(eness|iti)$"       "ive"
    #"(biliti|bli)$"        "ble"
    #"logi$"                "log"
    #"fulli$"               "ful"
    #"lessli$"              "less"
    #"(?<=[cdeghkmnrt])li$" ""))

(defn step-3 [word]
  (replace-longest-if in-r1? word
    #"tional$"         "tion"
    #"ational$"        "ate"
    #"alize$"          "al"
    #"ic(ate|iti|al)$" "ic"
    #"(ful|ness)$"     ""
    #"ative$"          #(if (in-r2? word #"ative$") "" %)))

(defn stem [word]
  (-> word
      (str/replace #"^'" "")
      (str/replace #"^y|(?<=[aeiouy]y)" "Y")
      step-0
      step-1a
      step-1b
      step-1c
      step-2
      step-3))
