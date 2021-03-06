(ns assignment1.core)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
;;  Question 1: Compute the total of a bill
;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn bill-total
  [bill]
  (if (empty? bill)
    0
    (+ (* ((first bill) :price) ((first bill) :quantity))
       (bill-total (rest bill)))))

(def bill [{:name "FroYo" :price 2 :quantity 4}
           {:name "Alligator" :price 3 :quantity 1}
           {:name "Whitney's Cajun-Style Gumbo" :price 2.5 :quantity 2}])

(bill-total bill)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
;;  Question 2: Add items to the bill
;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


(defn add-to-bill
  [bill items] 
  (if (empty? items)
    bill
    (let [t (first (filter #(= (% :name) ((first items) :name)) bill))
          ]
      (if (empty? (rest items))
        (if (empty? t)
          (conj bill (first items))
          (assoc bill (.indexOf bill t) 
                (assoc (first items) :quantity (+ ((first items) :quantity) (t :quantity)))))
        
        (if (empty? t)
          (add-to-bill (conj bill (first items)) (rest items))
          (add-to-bill (assoc bill (.indexOf bill t)
                            (assoc (first items) :quantity (+ ((first items) :quantity) (t :quantity))))
                            (rest items)))))))


(def items [{:name "FroYo" :price 2 :quantity 1}
            {:name "Alligator" :price 3 :quantity 4} 
            {:name "Coffee" :price 2.2 :quantity 1}]) 

(add-to-bill bill items)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
;;  Question 3: Write a higher order function 
;;  that will evaluate a polynomial
;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn make-poly 
  [polyvec]
  (partial (fn polynomial
             [polyvec input]
             (if (empty? polyvec)
               0
               (+ (* (Math/pow input (second (first polyvec))) (first (first polyvec)))
                  (polynomial (rest polyvec) input))))
            polyvec))

(def poly-vector [[1 2] [3 1] [-1 0]])

((make-poly poly-vector) 2)
(map (make-poly poly-vector) [0 1 2 3 4 5])

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
;;  Question 4: Write a function that will 
;;  return a vector that represents the 
;;  derivative of the input
;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn der
  [poly]
  (conj []
        (* (first poly) (second poly))
        (dec (second poly))))

(der [3 0])

(def differentiate
  (partial (fn diff [derivitive polyvec]
             (if (empty? polyvec)
               derivitive
               (if (> (second (first polyvec)) 0)
                 (diff (conj derivitive (der (first polyvec))) (rest polyvec))
                 derivitive))) 
           []))

(differentiate poly-vector)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
;;  Question 5: Write a function that
;;  approximates the root of a polynomial
;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn poly-eval
  [poly guess]
  (if (empty? poly)
    0
    (+ (* (Math/pow guess (second (first poly))) (first (first poly))) 
       (poly-eval (rest poly) guess))))

(defn find-root
  [tolerance poly guess]
  (let [p (poly-eval poly guess)]
   (if (< p tolerance)
     guess
     (find-root tolerance poly (- guess
                                  (/ p 
                                    (poly-eval (differentiate poly) guess)))))))

(def poly1 [[1 2] [2 1] [1 0]])
(def poly2 [[1 2] [-1 0]])
(def poly3 [[6 2] [1 1] [-1 0]])

(find-root 0.0001 poly1  10)
(find-root 0.0001 poly2  10)
(find-root 0.0001 poly2 -10)
(find-root 0.0001 poly3  10)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
;;  Question 6: Write functions that emulate
;;  deposits and withdrawals from a bank account
;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


(defn withdraw 
  [acct to-withdraw]
  (assoc acct :balance (- (acct :balance) to-withdraw)))

(defn deposit 
  [acct to-deposit]
  (assoc acct :balance (+ (acct :balance) to-deposit)))

(def bank-acct {:name "Ted Kassen" :acctno "814037036" :balance 5.00})

(withdraw bank-acct 5)
(deposit bank-acct 5000000)
