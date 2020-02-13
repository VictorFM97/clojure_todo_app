(ns todo-app.tasks.repository-test
  (:require [midje.sweet :refer :all]
            [todo-app.tasks.repository :as r]))

(facts "Functions of repository"
       (r/clear!)
       (fact "Adding new tasks"
             (let [task (r/add! {:title "abc" :description "def" :profile-id 1})]
               (and (= (:id task) 1)
                    (= (:profile-id task) 1)
                    (= (:title task) "abc")
                    (= (:description task) "def")
                    (false? (:deleted task))
                    (false? (:done task))
                    (inst? (:creation-date task))) => true)
             (let [task (r/add! {:title "123" :description "456" :profile-id 1})]
               (and (= (:id task) 2)
                    (= (:profile-id task) 1)
                    (= (:title task) "123")
                    (= (:description task) "456")) => true))
       (fact "Updating task should change the correct value"
             (r/update! 1 :done true)
             (:done (get (r/get-tasks) 0)) => true
             (r/update! 1 :done false)
             (:done (get (r/get-tasks) 0)) => false
             (r/update! 2 :deleted true)
             (:deleted (get (r/get-tasks) 1)) => true
             (r/update! 2 :deleted false)
             (:deleted (get (r/get-tasks) 1)) => false)
       (fact "Should return all tasks"
             (count (r/get-tasks)) => 2))