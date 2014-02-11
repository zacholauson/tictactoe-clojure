(ns ttt-clojure.core
  (:require [ttt-clojure.helper :refer :all]
            [ttt-clojure.gamestate :refer :all]
            [ttt-clojure.ai :refer :all]
            [ttt-clojure.display :refer :all]))

(defn get-next-move [gamestate]
  (cond
    (= (turn gamestate) :x) (find-move gamestate)
    (= (turn gamestate) :o) (ask-human-for-move gamestate)))

(defn format-board [gamestate]
  (doall (map println (partition 3 (:board gamestate)))))

(defn ttt [gamestate]
  (if (os-turn? gamestate) (format-board gamestate) nil)
  (cond
    (win?  gamestate :x) (prn "x won")
    (win?  gamestate :o) (prn "o won")
    (tied? gamestate   ) (prn "tied!")
    :else (ttt (move gamestate (get-next-move gamestate)))))

(defn -main []
  (ttt {:movelist [] :board [:- :- :- :- :- :- :- :- :-]}))
