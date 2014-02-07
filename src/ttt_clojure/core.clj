(ns ttt-clojure.core
  (:require [ttt-clojure.gamestate :refer :all]
            [ttt-clojure.display :refer :all]))

(defn get-next-move [gamestate]
  (cond
    (= (turn gamestate) "x") (ask-human-for-move gamestate)
    (= (turn gamestate) "o") (ask-human-for-move gamestate)))

(defn ttt [gamestate]
  (prn gamestate)
  (cond
    (win?  gamestate "x") (prn "x won")
    (win?  gamestate "o") (prn "o won")
    (tied? gamestate    ) (prn "tied!")
    :else (ttt (make-next-move gamestate (get-next-move gamestate)))))

(defn -main [] (ttt {:movelist [] :turn "x" :board ["-" "-" "-" "-" "-" "-" "-" "-" "-"]}))
