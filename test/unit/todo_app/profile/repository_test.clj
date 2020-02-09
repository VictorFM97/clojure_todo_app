(ns todo-app.profile.repository-test
  (:require [midje.sweet :refer :all]
            [todo-app.profile.repository :as r]))

(fact "Adding new profiles"
      (let [profile (r/add! {:name "Victor"})]
        (and (= (:id profile) 1)
             (= (:name profile) "Victor")
             (inst? (:creation-date profile))) => true)
      (let [profile (r/add! {:name "Carol"})]
        (and (= (:id profile) 2)
             (= (:name profile) "Carol")
             (inst? (:creation-date profile))) => true))

(fact "Should return all profiles"
      (count (r/get-profiles)) => 2)