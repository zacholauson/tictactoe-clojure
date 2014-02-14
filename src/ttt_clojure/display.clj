(ns ttt-clojure.display)

(defn format-space [space]
  (if (= (rest space) [:-]) (first space) (name (last space))))

(defn index-board [gamestate]
  (map format-space (map-indexed vector (:board gamestate))))

(defn format-board [gamestate]
  (map #(interpose "|" %) (vec (partition 3 (index-board gamestate)))))

(defn display-board [gamestate]
  (do
    (newline)
    (println (first (format-board gamestate)))
    (println "------------")
    (println (second (format-board gamestate)))
    (println "------------")
    (println (last (format-board gamestate)))
    (newline)))

(defn parse-int [string]
  (cond
    (string? string) (Integer. (re-find  #"\d+" string ))
    (integer? string) string
    :else (throw (Exception. "cannot convert given argument to integer"))))

(defn prompt
  ([prompt-message]
    (println (str prompt-message))
    (read-line))
  ([prompt-message validation]
    (try
      (println (str prompt-message))
      (let [user-input (read-line)]
           (if (validation user-input) user-input
               (prompt prompt-message validation)))
      (catch Exception e (prompt prompt-message validation))))
  ([prompt-message validation return-type]
    (println (str prompt-message))
    (try (let [user-input (read-line)]
              (let [parsed-input (cond (= return-type :int) (parse-int user-input)
                                       (= return-type :str) (str user-input)
                                       :else user-input)]
                   (if (validation parsed-input)
                       parsed-input
                       (prompt validation return-type))))
          (catch Exception e (prompt prompt-message validation return-type)))))

(defn valid-move? [gamestate move]
  (= :- (get (:board gamestate) move)))

(defn ask-human-for-move [gamestate]
  (prompt "Next Move: " #(valid-move? gamestate %) :int))

(defn includes? [matchers string]
  (some matchers (clojure.string/split string #"")))

(defn ask-for-who-should-go-first []
  (let [user-input (prompt "Who do you want to go first? (computer | human)")]
       (if (includes? #{"c" "C"} user-input) :computer :human)))
