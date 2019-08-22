; Lab 13: Final Review - Optional Questions

(define (has-cycle? s)
  (define (pair-tracker seen-so-far curr)
    (cond ((eq? seen-so-far curr) #t)
          ((contains? seen-so-far curr) (pair-tracker seen-so-far (cons curr (car (cdr-stream s)))))
          (else (pair-tracker (cons seen-so-far curr) (car (cdr-stream s)))))
    )
  (pair-tracker nil (car s)) 
)

(define (contains? lst s)
  (if (null? lst)
  	#f
  	(if (null? s)
  	#t
  	(if (= (car lst) (car s))
  		(contains? (cdr lst) (cdr s))
  		#f
  	)
  	)
  	)
)

(define-macro (switch expr cases)
    'YOUR-CODE-HERE
)