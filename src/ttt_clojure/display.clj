(ns ttt-clojure.display)

(defn parse-int [string]
  (cond
    (string? string) (Integer. (re-find  #"\d+" string ))
    (integer? string) string
    :else (throw (Exception. "cannot convert given argument to integer"))))

(defn valid-move? [gamestate move]
  (= :- (get (:board gamestate) move)))

(defn ask-human-for-move
  ([gamestate]
    (prn "Next Move: ")
    (let [user-move (read-line)]
      (if (valid-move? gamestate (parse-int user-move)) (parse-int user-move) (ask-human-for-move gamestate "Invalid Move, Try Again "))))
  ([gamestate error-message]
    (prn gamestate) (prn (str error-message))
    (let [user-move (read-line)]
      (if (valid-move? gamestate (parse-int user-move)) (parse-int user-move) (ask-human-for-move gamestate "Invalid Move, Try Again ")))))
