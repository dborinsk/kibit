(ns kibit.test.reporters
  (:require [kibit.reporters :as reporters]
            [clojure.string :as string]
            [clojure.test :refer :all]))

(defn- reported-lines [reporting]
  (->> (clojure.string/split reporting #"\n")
       (mapv #(clojure.string/replace % "\r" ""))
       (filterv (complement clojure.string/blank?))))

(deftest plain
  (are [check-map result]
       (= (reported-lines (with-out-str (reporters/cli-reporter check-map)))
          result)
       {:file "some/file.clj"
        :line 30
        :expr '(+ x 1)
        :alt '(inc x)} ["At some/file.clj:30:"
                        "Consider using:"
                        "  (inc x)"
                        "instead of:"
                        "  (+ x 1)"]))
(deftest gfm
  (are [check-map result]
       (= (reported-lines (with-out-str (reporters/gfm-reporter check-map)))
          result)
       {:file "some/file.clj"
        :line 30
        :expr '(+ x 1)
        :alt '(inc x)} ["----"
                        "##### `some/file.clj:30`"
                        "Consider using:"
                        "```clojure"
                        "  (inc x)"
                        "```"
                        "instead of:"
                        "```clojure"
                        "  (+ x 1)"
                        "```"]))
