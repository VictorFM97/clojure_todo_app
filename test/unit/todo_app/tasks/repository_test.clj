(ns todo-app.tasks.repository-test
  (:require [midje.sweet :refer :all]
            [todo-app.tasks.repository :as r]))

(facts "adding"
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
                    (= (:description task) "456")) => true)))

(fact "Should return all tasks"
      (count (r/get-tasks)) => 2)