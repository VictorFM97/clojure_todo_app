(ns todo-app.tasks.business-test
  (:require [midje.sweet :refer :all]
            [todo-app.tasks.business :as b]))

(def example-tasks [{:id 1 :profile-id 1 :title "abc" :description "def"}
                    {:id 2 :profile-id 1 :title "abc" :description "def"}
                    {:id 3 :profile-id 2 :title "abc" :description "def"}
                    {:id 4 :profile-id 3 :title "abc" :description "def"}])

(facts "Add! function"
       (fact "Doesn't insert with missing title"
             (b/add! {:description "abc" :profile-id 1}) => false)
       (fact "Doesn't insert with missing description"
             (b/add! {:title "abc" :profile-id 1}) => false)
       (fact "Doesn't insert with missing profile-id"
             (b/add! {:title "abc" :description "abc"}) => false))

(facts "filtering"
       (with-redefs [b/filter-tasks (fn [pred]
                                      (filter pred example-tasks))]
         (fact "get-all"
               (fact "Should return filtered results"
                     (count (b/get-all 1)) => 2
                     (count (b/get-all 2)) => 1
                     (count (b/get-all 3)) => 1))
         (fact "get-most-recent"
               (fact "Should get last entry added for id"
                     (let [task (b/get-most-recent 1)]
                       (and (not (nil? task))
                            (= (:id task) 2)
                            (= (:title task) "abc")
                            (= (:description task) "def")
                            (= (:profile-id task) 1)) => true)
                     (let [task (b/get-most-recent 2)]
                       (and (not (nil? task))
                            (= (:id task) 3)
                            (= (:title task) "abc")
                            (= (:description task) "def")
                            (= (:profile-id task) 2)) => true)
                     (let [task (b/get-most-recent 3)]
                       (and (not (nil? task))
                            (= (:id task) 4)
                            (= (:title task) "abc")
                            (= (:description task) "def")
                            (= (:profile-id task) 3)) => true)))
         (fact "Should return nil if there are no tasks for x profile"
               (let [task (b/get-most-recent 4)]
                 task => nil))
         (fact "get-by-id"
               (fact "Should find a specific task"
                     (let [task (b/get-by-id 1)]
                       (and (not (nil? task))
                            (= (:id task) 1)) => true)
                     (let [task (b/get-by-id 4)]
                       (and (not (nil? task))
                            (= (:id task) 4)) => true))
               (fact "Should return nil if task doesn't exists"
                     (let [task (b/get-by-id 999)]
                       task => nil)))))