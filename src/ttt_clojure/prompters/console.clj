(ns ttt-clojure.prompters.console
  (:require [ttt-clojure.interface.prompter :refer :all]
            [ttt-clojure.interface.display  :refer :all]))

(defn clear-screen []
  (print "\u001b[2J")
  (print "\u001B[0;0f"))

(defn parse-int [string]
  (cond
    (string? string) (Integer. (re-find  #"\d+" string ))
    (integer? string) string
    :else (throw (Exception. "cannot convert given argument to integer"))))

(defn parse-input [input output-type]
  (case output-type
    :int (parse-int input)
    :str (str input)))

(defn valid-move? [gamestate move]
  (= :- (get (:board gamestate) move)))

(defn format-option [option]
  (apply str (first option) " : " (rest option)))

(defn prompt
  ([display output-str]
    (output display (str output-str))
    (read-line))

  ([display output-str validation return-type]
    (output display (str output-str))
    (let [user-input (read-line)]
         (let [parsed-input (parse-input user-input return-type)]
              (if (validation parsed-input)
                  parsed-input
                  (prompt display output-str validation return-type)))))

  ([display output-str option-collection]
    (clear-screen)
    (output display (str output-str))
    (let [numbered-options (apply merge (map-indexed hash-map option-collection))]
      (loop [options numbered-options]
        (output display (format-option (first options)))
          (if-not (empty? (rest options))
            (recur (rest options))
            (let [user-input (parse-int (read-line))
                  option-keys (keys numbered-options)]
              (if (some #{user-input} option-keys)
                (get numbered-options user-input)
                (prompt display output-str option-collection))))))))

(deftype Console [-display]
  Prompter
  (ask [this output-str option-collection]
    (prompt -display  output-str option-collection))
  (ask-for-move [this gamestate]
    (prompt -display "Next Move: " #(valid-move? gamestate %) :int)))

(defn new-console-prompter [display]
  (Console. display))
