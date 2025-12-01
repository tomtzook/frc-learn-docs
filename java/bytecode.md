You may already be aware of the fact that _Java_ code runs on the _Java Virtual Machine_. And if you are familiar with other 
languages, you may also be aware that other languages do not have their own _Virtual Machine_. Well, this is not entirely true, _C#_ has
its own equivalent, but languages like _C_ or _Rust_, which we call _native_ languages, do not.

So why is that? We are here to day to discuss this exact question.

## The Problem

If you've ever learned about the internals of how programming and code works, you might be familiar with the concept of _Machine Code_. This term
refers to the fact that computer processors (_CPU_s) do not run code as a text, but rather run code after it was transformed into _Machine Code_. And this
_Machine Code_ is in fact just a set of binary data representing what actions the _CPU_ has to perform.

But _Machine Code_ can be very different between two different _CPU_s. It is different enough that _Machine Code_ written for one may not work at all
on another.

The _Machine Code_ for performing an adding of `1 + 1` on _Intel_'s _x86_ architecture and on _ARM_'s _aarch64_ look quite different
```
// Intel x86: mov rax, 1
48 83 c0 01

// Arm AArch64: ADD X0, X1, X2
20 00 02 8b 
```

### CPU Architecture

A processor is basically a large set of logic circuits connected to each other and other components. It is capable of taking instructions and 
execute them one by one. Its architecture is the design and capabilities of a processor, which determines its performance and how it executes 
software through its instruction set. So when a company makes a processor, they typically come up with their own architecture. And over time they are also like to modify this architecture or switch
to a new one entirely. 

Some examples for architectures include
- _Intel_'s _x86_/_x86-64_ - common on desktop machines
- _Arm_'s _armv7_ - common on smartphones
- _Arm_'s _aarch64_ - common on smartphones
- _Apple_'s _PowerPC_ - common on apple products
- And more

Then although processors seem very similar, internally, at the lowest levels, there are many differences. Both in what instructions they are capable to perform
and how they do it.

If we look back at the _Machine Code_ example I gave above, we can see that not only was the _Machine Code_ different between the architecture, but
there was also a difference in the behaviour of the instructions:
- _x86_'s instruction was `mov rax, 1` which basically does `rax += 1`
- _aarch64_'s instruction was `add x0, x1, x2` which basically does `x0 = x1 + x2`

So the two instruction sets are very different from one another, and thus the code will be very different.

The lowest level text language is called _assembly_, which is what we saw next to the _Machine Code_. It is actually not a single language, but refers to multiple different subsets. What's common
between them is that they are all basically human-readable versions of _Machine Code_. So in _assembly_ if I want to perform addition I use the instruction name `add`, 
while in _Machine Code_ it is represented by a number. To transform _assembly_ into _Machine Code_ I just need to use an _assembler_ tool.

But of course, because of the difference of architectures and instruction sets, assembly would be written differently for each processor, and would result in different _Machine Code_. Meaning
that I must write my code to suit the specific processor I am to use.

Here are some example of a function adding too numbers in various instruction sets
```
// original c code
int add(int num1, int num2) {
    return num1 + num2;
}

// x86-64
add(int, int):
  lea eax, [rdi + rsi]
  ret

// armv7-a
add(int, int):
  add r0, r1, r0
  bx lr

// aarch64
add(int, int):
  add w0, w1, w0
  ret

// AVR
add(int, int):
.L__stack_usage = 0
  add r24,r22
  adc r25,r23
  ret

// mips
add(int, int):
$func_begin0 = $tmp0
  jr $ra
  addu $2, $5, $4
```

### OS

### Higher Level Languages

When we take text code of a _native_ language like _C_ and want to transform it into _Machine Code_, it has to go through two processes:
- Compiling: transform the code into assembly instructions
- Assembling: transforms the assembly instructions into machine code

This two step transformation is because _assembly_ is basically a human-readable version of _Machine Code_, which already existed when higher level
languages came along. So this process became easier by breaking it into two parts.
