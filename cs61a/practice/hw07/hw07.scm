
(define (cddr s)
  (cdr (cdr s)))

(define (cadr s)
  (car (cdr s))
)

(define (caddr s)
  (car (cddr s))
)

(define (sign x)
  (cond 
  	((< x 0) -1)
  	((= x 0) 0)
  	(else 1)
  	)
)

(define (square x) (* x x))

(define (pow b n)
  (cond
  	((zero? n) 1)
  	((even? n) (square (pow b (/ n 2))))
  	((odd? n) (* b (square (pow b (/ (- n 1) 2)))))
  )
)

(define (ordered? s)
  (if (null? (cdr s))
  	True
  	(if (< (cadr s) (car s))
  		False
  		(ordered? (cdr s))
  	)
  )
)