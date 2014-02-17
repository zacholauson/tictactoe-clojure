(ns ttt-clojure.core
  (:require [ttt-clojure.gamestate :refer :all]
            [ttt-clojure.ai :refer :all]
            [ttt-clojure.display :refer :all]))

(defn get-next-move [gamestate]
  (if (computers-turn? gamestate) (find-move gamestate)
      (ask-human-for-move gamestate)))

(defn ttt [gamestate]
  (if (not (computers-turn? gamestate)) (display-board gamestate))
  (cond
    (win?  gamestate :x) (prn "x won")
    (win?  gamestate :o) (prn "o won")
    (tied? gamestate   ) (prn "tied!")
    :else (ttt (move gamestate (get-next-move gamestate)))))

(defn -main []
  (let [who-goes-first (ask-for-who-should-go-first)
        difficulty-setting (ask-for-difficulty)]
       (ttt {:board [:- :- :- :- :- :- :- :- :-]
             :computer (if (= who-goes-first :computer) :x :o)
             :options {:difficulty difficulty-setting}})))
