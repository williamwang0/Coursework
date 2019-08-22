;; Extra Scheme Questions ;;


; Q5
(define lst
  (cons (list 1) (cons 2 (cons (list 3 4) (cons 5 nil))))
)

; Q6
(define (composed f g)
  (lambda (x) (f (g x)))
)

; Q7
(define (remove item lst)
  (filter (lambda (x) (not (= x item))) lst)
)


;;; Tests
(remove 3 nil)
; expect ()
(remove 3 '(1 3 5))
; expect (1 5)
(remove 5 '(5 3 5 5 1 4 5 4))
; expect (3 1 4 4)

; Q8
(define (no-repeats s)
  (if (null? s)
  	()
  	(cons (car s) (filter (lambda (x) (not (= x (car s)))) (no-repeats (cdr s))))
  )
)

; Q9
(define (substitute s old new)
  (if (null? s) 
  	s
  	(if (pair? (car s))
  		(cons (substitute (car s) old new) (substitute (cdr s) old new))
  		(if (equal? old (car s))
  			(cons new (substitute (cdr s) old new))
  			(cons (car s) (substitute (cdr s) old new))
  		)
  	)
  )
)

; Q10
(define (sub-all s olds news)
  (if (null? olds)
  	s
  	(sub-all (substitute s (car olds) (car news)) (cdr olds) (cdr news))
  )
)