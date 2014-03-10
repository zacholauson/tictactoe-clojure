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
