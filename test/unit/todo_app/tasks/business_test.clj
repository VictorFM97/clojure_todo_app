(ns todo-app.tasks.business-test
  (:require [clojure.test :refer :all]
            [todo-app.tasks.business :as b]))

(def example-tasks [{:id 1 :profile-id 1 :title "abc" :description "def"}
                    {:id 2 :profile-id 1 :title "abc" :description "def"}
                    {:id 3 :profile-id 2 :title "abc" :description "def"}
                    {:id 4 :profile-id 3 :title "abc" :description "def"}])

(deftest adding
  (testing "Add! function"
    (testing "Doesn't insert with missing title"
      (is (= (b/add! {:description "abc" :profile-id 1}) "Invalid task")))
    (testing "Doesn't insert with missing description"
      (is (= (b/add! {:title "abc" :profile-id 1}) "Invalid task")))
    (testing "Doesn't insert with missing profile-id"
      (is (= (b/add! {:title "abc" :description "abc"}) "Invalid task")))))

(deftest filtering
  (with-redefs [b/filter-tasks (fn [pred]
                                 (filter pred example-tasks))]
    (testing "get-all"
      (testing "Should return filtered results"
        (is (= 2 (count (b/get-all 1))))
        (is (= 1 (count (b/get-all 2))))
        (is (= 1 (count (b/get-all 3))))))
    (testing "get-most-recent"
      (testing "Should get last entry added for id"
        (let [task (b/get-most-recent 1)]
          (is (and (not (nil? task))
                   (= (:id task) 2)
                   (= (:title task) "abc")
                   (= (:description task) "def")
                   (= (:profile-id task) 1))))
        (let [task (b/get-most-recent 2)]
          (is (and (not (nil? task))
                   (= (:id task) 3)
                   (= (:title task) "abc")
                   (= (:description task) "def")
                   (= (:profile-id task) 2))))
        (let [task (b/get-most-recent 3)]
          (is (and (not (nil? task))
                   (= (:id task) 4)
                   (= (:title task) "abc")
                   (= (:description task) "def")
                   (= (:profile-id task) 3)))))
      (testing "Should return nil if there are no tasks for x profile"
        (let [task (b/get-most-recent 4)]
          (is (nil? task)))))
    (testing "get-by-id"
      (testing "Should find a specific task"
        (let [task (b/get-by-id 1)]
          (is (and (not (nil? task))
                   (= (:id task) 1))))
        (let [task (b/get-by-id 4)]
          (is (and (not (nil? task))
                   (= (:id task) 4))))
        (testing "Should return nil if task doesn't exists"
          (let [task (b/get-by-id 999)]
            (is (nil? task))))))))