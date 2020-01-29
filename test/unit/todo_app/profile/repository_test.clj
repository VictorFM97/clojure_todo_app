(ns todo-app.profile.repository-test
  (:require [clojure.test :refer :all]
            [todo-app.profile.repository :as r]))

(deftest adding
  (testing "Adding new profiles"
    (let [profile (r/add! {:name "Victor"})]
      (is (and (= (:id profile) 1)
               (= (:name profile) "Victor")
               (inst? (:creation-date profile)))))
    (let [profile (r/add! {:name "Carol"})]
      (is (and (= (:id profile) 2)
               (= (:name profile) "Carol")
               (inst? (:creation-date profile)))))))

(deftest get-profiles
  (testing "Should return all profiles"
    (is (= (count (r/get-profiles)) 2))))

(r/clear!)