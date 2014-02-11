(ns ttt-clojure.core
  (:require [ttt-clojure.gamestate :refer :all]
            [ttt-clojure.ai :refer :all]
            [ttt-clojure.display :refer :all]))

(defn get-next-move [gamestate]
  (if (xs-turn? gamestate) (find-move gamestate)
      (ask-human-for-move gamestate)))

(defn ttt [gamestate]
  (if (os-turn? gamestate) (format-board gamestate))
  (cond
    (win?  gamestate :x) (prn "x won")
    (win?  gamestate :o) (prn "o won")
    (tied? gamestate   ) (prn "tied!")
    :else (ttt (move gamestate (get-next-move gamestate)))))

(defn -main []
  (ttt {:movelist [] :board [:- :- :- :- :- :- :- :- :-]}))
