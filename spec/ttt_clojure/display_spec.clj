(ns ttt-clojure.display-spec
  (:require [speclj.core :refer :all]
            [ttt-clojure.display :refer :all]))

(describe "parse-int"
  (it "should return the integer version of the string passed to it"
    (should= 1 (parse-int "1")))
  (it "should return an integer if given an integer"
    (should= 1 (parse-int 1)))
  (it "should throw an exception if given argument cannot be parsed"
    (should-throw Exception "cannot convert given argument to integer" (parse-int ["test"]))))

(describe "valid-move?"
  (let [gamestate {:movelist [] :turn "x" :board ["-" "-" "-" "-" "-" "-" "-" "-" "-"]}]
  (it "should return true if the given move is valid with the current gamestate"
    (should= true (valid-move? gamestate 0)))))
