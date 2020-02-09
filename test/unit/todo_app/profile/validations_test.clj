(ns todo-app.profile.validations-test
  (:require [midje.sweet :refer :all]
            [todo-app.profile.validations :as v]))

(facts "valid"
       (fact "Should return false when profile is invalid"
             (v/valid? {}) => false
             (v/valid? {:name nil}) => false
             (v/valid? {:name 1}) => false)
       (fact "Should return true when profile is valid"
             (v/valid? {:name "Victor"}) => true))