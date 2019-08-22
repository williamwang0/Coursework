


(define-macro (def func bindings body)
    `(define ,func (lambda ,bindings ,body)))


(define-macro (or-macro expr1 expr2)
    `(let ((v1 ,expr1))
        (if v1 v1 ,expr2)))

(define (flatmap f x)
  (define (help lst1 lst2)
    (if (null? lst1)
      lst2
      (help (cdr lst1) (append lst2 (f (car lst1))))
    )
  )
  (help x nil)
)

(define (expand lst)
  'YOUR-CODE-HERE)

(define (interpret instr dist)
  'YOUR-CODE-HERE)

(define (apply-many n f x)
  (if (zero? n)
      x
      (apply-many (- n 1) f (f x))))

(define (dragon n d)
  (interpret (apply-many n expand '(f x)) d))