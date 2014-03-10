(ns ttt-clojure.tic-tac-toe-spec
  (:require [speclj.core                    :refer :all]
            [ttt-clojure.tic-tac-toe        :refer :all]
            [ttt-clojure.interface.player   :refer :all]
            [ttt-clojure.players.computer   :refer [new-computer]]
            [ttt-clojure.players.human      :refer [new-human]]
            [ttt-clojure.interface.display  :refer :all]
            [ttt-clojure.displays.console   :refer [new-display-console]]
            [ttt-clojure.interface.prompter :refer :all]
            [ttt-clojure.prompters.console  :refer [new-console-prompter]]))

(def display  (new-display-console))
(def prompter (new-console-prompter display))

(describe "#get-next-move"
  (context "computers turn"
    (let [computer (new-computer :x)
          human    (new-human :o prompter)
          gamestate {:board [:- :- :- :- :- :- :- :- :-] :players [computer human] :computer :x :options {:difficulty :unbeatable}}]
      (it "should ask the correct player for the next move"
        (should= 0 (get-next-move gamestate))))))
  ;; (context "humans turn"
  ;;   (around [it]
  ;;     (with-out-str (it)))
  ;;
  ;;   (let [computer (new-computer :o)
  ;;         human    (new-human :x prompter)
  ;;         gamestate {:board [:- :- :- :- :- :- :- :- :-] :players [human computer] :computer :o :options {:difficulty :unbeatable}}]
  ;;     (it "should ask the human what their next move should be"
  ;;       (should= "Next Move: \n"
  ;;         (with-out-str (with-in-str "1"
  ;;           (get-next-move gamestate))))
  ;;       (should= 1
  ;;         (with-in-str "1"
  ;;           (get-next-move gamestate)))))))

