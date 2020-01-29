(ns todo-app.tasks.repository-test
  (:require [clojure.test :refer :all]
            [todo-app.tasks.repository :as r]))

(deftest Adding
  (testing "Adding new tasks"
    (let [task (r/add! {:title "abc" :description "def" :profile-id 1})]
      (is (and (= (:id task) 1)
               (= (:profile-id task) 1)
               (= (:title task) "abc")
               (= (:description task) "def")
               (false? (:deleted task))
               (false? (:done task))
               (inst? (:creation-date task)))))
    (let [task (r/add! {:title "123" :description "456" :profile-id 1})]
      (is (and (= (:id task) 2)
               (= (:profile-id task) 1)
               (= (:title task) "123")
               (= (:description task) "456"))))))

(deftest GetTasks
  (testing "Should return all tasks"
    (is (= (count (r/get-tasks)) 2))))

(r/clear!)