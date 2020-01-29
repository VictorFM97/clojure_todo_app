(ns todo-app.profile.validations-test
  (:require [clojure.test :refer :all]
            [todo-app.profile.validations :as v]))

(deftest valid
  (testing "Should return false when profile is invalid"
    (is (false? (v/valid? {})))
    (is (false? (v/valid? {:name nil})))
    (is (false? (v/valid? {:name 1})))
    (is (false? (v/valid? {:name ""}))))
  (testing "Should return true when profile is valid"
    (is (v/valid? {:name "Victor"}))))