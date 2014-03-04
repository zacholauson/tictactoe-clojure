(defproject ttt-clojure "0.1.1-SNAPSHOT"
  :description "unbeatable tic tac toe"
  :url "https://github.com/zacholauson/tictactoe-clojure"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/math.numeric-tower "0.0.4"]
                 [org.clojure/data.json "0.2.4"]]
  :profiles {:dev {:dependencies [[speclj "2.9.1"]]}}
  :plugins [[speclj "2.9.1"]]
  :test-paths ["spec"]
  :main ttt-clojure.core)
