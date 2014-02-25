(ns ttt-clojure.prompters.console-spec
  (:require [speclj.core                   :refer :all]
            [ttt-clojure.prompters.console :refer :all]
            [ttt-clojure.interface.display :refer :all]
            [ttt-clojure.displays.console  :refer [new-display-console]]))

(def display (new-display-console))

(describe "#parse-int"
  (it "should convert the given input to a integer if possible"
    (should= 1 (parse-int "1"))
    (should= 1 (parse-int  1 )))
  (it "should throw an exception if the given value cannot be converted to an integer"
    (should-throw Exception (parse-int "abc"))))

(describe "#parse-input"
  (it "should return the input parsed to the given format"
    (should=  1  (parse-input "1" :int))
    (should=  1  (parse-input  1  :int))
    (should= "1" (parse-input  1  :str))))

(describe "#valid-move?"
  (it "should return true or false depending on if the move given is valid for the given gamestate"
    (should= true  (valid-move? {:board [:- :- :- :- :- :- :- :- :-]} 0))
    (should= false (valid-move? {:board [:x :- :- :- :- :- :- :- :-]} 0))))

(describe "#prompt-for-who-should-go-first"
  (around [it]
    (with-out-str (it)))

  (it "should ask for player who should go first"
    (should= :computer
      (with-in-str "1"
        (prompt-for-who-should-go-first display)))
    (should= :human
      (with-in-str "2"
        (prompt-for-who-should-go-first display)))))

(describe "#prompt-for-difficulty"
  (around [it]
    (with-out-str (it)))

  (it "should ask for the difficulty of the game you want to play"
    (should= :unbeatable
      (with-in-str "1"
        (prompt-for-difficulty display)))
    (should= :medium
      (with-in-str "2"
        (prompt-for-difficulty display)))
    (should= :easy
      (with-in-str "3"
        (prompt-for-difficulty display)))))

(describe "#prompt-for-board-size"
  (around [it]
    (with-out-str (it)))

  (it "should ask for the board size you want to play on"
    (should= 3
      (with-in-str "3"
        (prompt-for-board-size display)))
    (should= 4
      (with-in-str "4"
        (prompt-for-board-size display)))
    (should= 5
      (with-in-str "5"
        (prompt-for-board-size display)))))
