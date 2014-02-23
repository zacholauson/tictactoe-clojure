(ns ttt-clojure.display-spec
  (:require [speclj.core :refer :all]
            [ttt-clojure.display :refer :all]))

(describe "prompt"
  (around [it]
    (with-out-str (it)))

  (it "should ask for input and return the user-input"
    (should= "1"
      (with-in-str "1"
        (prompt "Insert a number"))))
  (it "should ask for input and return the user-input if passes given validation"
    (should= "1"
      (with-in-str "1"
        (prompt "Insert a string" #(string? %)))))
  (it "should ask for input and return the user-input if passes validation and should return value in given type"
    (should= 1
      (with-in-str "1"
        (prompt "Insert an integer" #(integer? %) :int)))))

(describe "parse-int"
  (it "should return the integer version of the string passed to it"
    (should= 1 (parse-int "1")))
  (it "should return an integer if given an integer"
    (should= 1 (parse-int 1)))
  (it "should throw an exception if given argument cannot be parsed"
    (should-throw Exception "cannot convert given argument to integer" (parse-int ["test"]))))

(describe "valid-move?"
  (let [gamestate {:board [:- :- :- :- :- :- :- :- :-]}]
    (it "should return true if the given move is valid with the current gamestate"
      (should= true (valid-move? gamestate 0)))))

(describe "ask-for-who-should-go-first"
  (around [it]
    (with-out-str (it)))

  (it "should ask for who should go first and return the chosen option"
    (should= :computer
      (with-in-str "1"
        (ask-for-who-should-go-first)))
    (should= :human
      (with-in-str "2"
        (ask-for-who-should-go-first)))))

(describe "ask-for-difficulty"
  (around [it]
    (with-out-str (it)))

  (it "should ask for the difficulty you want to play and return the chosen option"
    (should= :unbeatable
      (with-in-str "1"
        (ask-for-difficulty)))
    (should= :medium
      (with-in-str "2"
        (ask-for-difficulty)))
    (should= :easy
      (with-in-str "3"
        (ask-for-difficulty)))))

(describe "ask-for-board-size"
  (around [it]
    (with-out-str (it)))

  (it "should ask for the board size you want to play and then return the chosen board size"
    (should= 3
      (with-in-str "3"
        (ask-for-board-size)))
    (should= 4
      (with-in-str "4"
        (ask-for-board-size)))
    (should= 5
      (with-in-str "5"
        (ask-for-board-size)))))
