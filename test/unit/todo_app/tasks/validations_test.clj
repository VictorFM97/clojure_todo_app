(ns todo-app.tasks.validations-test
  (:require [clojure.test :refer :all]
            [todo-app.tasks.validations :as v]))

(def task {:title "abc"
           :description "def"
           :profile-id 1
           :deleted false})

(deftest valid
  (testing "Is task valid"
    (is (true? (v/valid? task))))
  (testing "Is task invalid"
    (is (false? (v/valid? {})))
    (is (false? (v/valid? (assoc task :title nil))))
    (is (false? (v/valid? (assoc task :title ""))))
    (is (false? (v/valid? (assoc task :title 1))))
    (is (false? (v/valid? (assoc task :description nil))))
    (is (false? (v/valid? (assoc task :description ""))))
    (is (false? (v/valid? (assoc task :description 1))))
    (is (false? (v/valid? (assoc task :profile-id "1"))))
    (is (false? (v/valid? (assoc task :profile-id nil))))
    (is (false? (v/valid? (assoc task :profile-id 0))))))

(deftest IsDeleted
  (testing "Is task deleted"
    (is (false? (v/deleted? task)))
    (is (true? (v/deleted? (assoc task :deleted true))))))