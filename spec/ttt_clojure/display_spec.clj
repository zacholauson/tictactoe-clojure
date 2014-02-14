(ns ttt-clojure.display-spec
  (:require [speclj.core :refer :all]
            [ttt-clojure.display :refer :all]))

(describe "prompt"
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
