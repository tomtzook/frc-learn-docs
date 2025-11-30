All programs, from the lowest to the heighest level, require some form of data storage to use. This is typically provided by the _Random Access Memory_ card
connected to the motherboard. We can look at memory as a long contiguous space where we can store information in granuality of at least a single byte, with each
_byte_ of memory being referred to by an address unique to it

<img width="505" height="304" alt="image" src="https://github.com/user-attachments/assets/ea6c4f5e-d0d5-48d4-b0e2-88ccc3b4ffd8" />

We can then fill this space of memory with our data, _allocating_ a set of bytes as needed by the type of data (**int**, **String**, etc). Data is not the only thing
populating this memory, code is also loaded into this memory, just instead of values of data we get the code _instructions_. But there
is a problem here: we need a way to manage access and allocation of this memory space.

> [!NOTE]
> When compiled, code is basically a set of instructions to the processor of the computer (things like _add_ or _read memory_).
> So when assembled into computer code (native/binary) these instructions are represented by numbers (the instruction for `return` on _x86 Intel_ CPUs is `C3` in hex and `11000011` in binary).

Consider the following questions:
- I am the code in a function; Where can I find the value of a parameter passed to me?
- I am the code in a function with `return`; Where do I return to? i.e. what is the address of the code that I need to return to?
- I want to call a member function of a class; Where can I find the memory address of its code?
- I want to create a new instance of a class, in what address of memory should I put the data?
- I want to create a new **int** variable; Where do I put its value?

These questions refer to basic needs of any program and there answer depends on how memory is managed in the program. In this article we will
go over how this is done in _Java_.

## General Memory Management

Programs on an operating system will typically receive a contiguous (_virtual_) memory space to store all its stuff. This includes both code and data
which are split into two distinct areas on purpose.

**FINISH**

## Data Memory

Data memory, which is memory used to store information, not code, is generally divided into two distinct structures: _The Heap_ and _The Stack_.
Each of these was made for a very specific set of uses, meaning that a program will need to use both and can rarely rely on just one of them.

Most program data memory is both readable and writable, meaning my program can read the information in the memory and write information into the memory. But not all memory
is such, some, typically _global const_ memory is typically only readable, but this is not of interest to us at the moment.

### Stack Memory

The _stack_ memory, named so after its data structure - a stack, is used to store temporary local information as well as method call information (leading to it sometimes called _call stack_).

#### Structure

The stack structure is basic and normally defined by the CPU of the machine. Althoug the structure is very common between different architectures, it is not necessarily the exact same.

<img width="276" height="342" alt="image" src="https://github.com/user-attachments/assets/8b21ecbb-1963-4e46-85cb-852a4adb4d48" />

There are several important characteristics to understand about the stack and how it works:
- The stack of the program will receive a memory space when the program is created. This stack can be increased in size when needed.
- The stack has a position indicator (`RSP` on _x86 Intel_) which stores the next address to put memory at. It starts at the start of the stack.
- Memory can be _pushed_ or _popped_ into the stack.
  - Pushing memory puts new data at the new position (based on `RSP`) and modifies the position indicator to the next address
  - Popping memory reads the last data placed on the stack (according to the position indicator) and changes the position indicator to be before this info.
  - One can actually read and write anywhere in the stack, but it is worthless without knowing _where_ to do so
- The stack start position is actually at the _top_ memory address and ends at the lower memory address. Essentially making the stack _grow downwards_.

This can be rather confusing, so lets make a simple example to illustrate this.

We start the program with the stack starting at address `2000` and ends at address `1000`. The stack pointer will start at `2000`.

<img width="247" height="162" alt="image" src="https://github.com/user-attachments/assets/360824f0-b111-4074-b504-f1def5c0bd00" />

Now we start by writing data into the next stack position by doing `push 5`, which writes 4 bytes of information with the value `5` (`000000000101`) into the current stack pointer (address `2000`). 
The information is written on bytes at addresses `2000`, `1999`, `1998`, `1997`. The new stack pointer will be at position `1996`.

<img width="266" height="154" alt="image" src="https://github.com/user-attachments/assets/e3743270-0008-4deb-bb9e-819a38ad8608" />

Next, let's write another 4 bytes of information with `push 10`, which writes 4 bytes of information with the value `5` into the current stack pointer (address `1996`).
The information is written on bytes at addresses `1996`, `1995`, `1994`, `1993`. The new stack pointer will be at position `1992`.

<img width="268" height="151" alt="image" src="https://github.com/user-attachments/assets/70c8a21e-891e-4f42-9465-f92fd74eca64" />

We have stored a total of 8 bytes on the stack so far, divided into 2 different values. We can actually read both of them by just accessing their bytes directly and reading the values (bytes `2000`, `1999`, `1998`, `1997` for value `5`).
When a value is no longer of use in the stack, we would pop it off the stack (basically deleting it) with `pop`. The stack pointer after this will be moved back to `1996`, since we popped 4 bytes of information out, essentially returning to the previous state.

<img width="266" height="154" alt="image" src="https://github.com/user-attachments/assets/e3743270-0008-4deb-bb9e-819a38ad8608" />

> [!NOTE]
> In reality, poping doesn't completely delete the information. For one, when poping the popped value will be placed in a CPU register (a small temporary storage space).
> But there is also the fact that popping doesn't clear the contents of the memory, just moves the stack pointer, so we could still read those bytes and find the old values (unless overwritten).

> [!NOTE]
> In the example above we showed only pushing and popping 4 bytes of data. In reality it is possible to do so with any size of data, as long as you know which size you need.
> It is also worth notice that `push` and `pop` aren't the only way to modify the stack or the stack pointer, but that's out of scope here.

#### Use

The stack is used to hold the information for _function calls_ and _local variables_. Essentially, every time a function is called,
the stack is updated with some information relevant to this call, including the parameters (dependent) and the return address. Any local variable 
created in the function will be normally stored in the stack, as will some return values for _non_-**void** functions.

Let us again look at an example to illustrate how this works.

```Java
public static void main(String[] args) {
  int result = func(55, 21);
  // <---- the return address after add
  System.out.println(result);  
}

static int func(int num1, int num2) {
  int result = num1 + num2;
  return result;
}
```

This is the compiled __Byte Code__ of the code above
```
main:
 0 bipush 55
 2 bipush 21
 4 invokestatic #7 <Main.func : (II)I>
 7 istore_1
 8 getstatic #13 <java/lang/System.out : Ljava/io/PrintStream;>
11 iload_1
12 invokevirtual #19 <java/io/PrintStream.println : (I)V>
15 return

func:
0 iload_0
1 iload_1
2 iadd
3 istore_2
4 iload_2
5 ireturn
```

We start with some stack with the stack pointer at position `0x2000`, and start the code on the call to `func` function.
This will place the following on the stack
- first parameter, `num1`, value given is `55` in 4 bytes, _bytecode_: `bipush 55` (conditional)
- second parameter, `num2`, value given is `21` in 4 bytes, _bytecode_: `bipush 21` (conditional)
- space for placing the return value from the function, 4 bytes, not seen in the code.
- the intended return address from `add`, which is to line `7` in the bytecode (done as part of `invokestatic`).

The final stack pointer will be `0x2000 - (3 * 4) - 8` (3 * `int` + 1 `address`).

<img width="271" height="147" alt="image" src="https://github.com/user-attachments/assets/101b3ae3-4f45-4848-8bdb-bba99602ec49" />

Then when the code in the function will read the parameters from the stack (by calculating what the address will be), add them together, save the result and return to the stored address.
This is not entirely clear from the byte code, because it is byte-code, so let's show it differently in an assembly code
```asm
main:
  ... # the code before

1   sub rsp, 12                      # clear space for the 2 parameters + return value
2   mov dword [rsp + 8], 55          # put 55 at the first parameter location
3   mov dword [rsp + 4], 21          # put 21 at the second parameter location
4   call func                        # calls the function, will place the return address on the stack
5   mov rax, dword [rsp]             # function will return to here, read the return value from the stack
6   add rsp, 12                      # restore the stack pointer to before the call

  ... # the code after

func:
7   mov rax, dword [rsp + 8 + 4 + 4] # read the first parameter into rax.
                                     # Need to skip the return address, the second parameter and the return value location

8   mov rcx, dword [rsp + 8 + 4]     # read the first parameter into rcx.
                                     # Need to skip the return address and the return value location

9   add rax, rcx                     # add rax and rcx. result will be in rax

10  mov dword [rsp + 8], rax         # write the result from rax into the location on stack for the return value
11  ret                              # return to the return address on stack (location rsp)
```

This may look complex, but that's just assembly being assembly. Let's go over it line by line with the stack.

We start at line 1, with the stack being at its initial state for us.

<img width="254" height="157" alt="image" src="https://github.com/user-attachments/assets/b9df2f94-baf1-4480-90df-19290b955921" />

Line 1: This line just moves the stack pointer 12 bytes

<img width="245" height="159" alt="image" src="https://github.com/user-attachments/assets/c3c1344a-2bba-4b9f-b290-5d685a783c82" />

Line 2: Here we put 55 into the stack at bytes `0x1ffc`-`0x1fff`

<img width="272" height="158" alt="image" src="https://github.com/user-attachments/assets/ed95d8f6-98e3-4c68-8305-b7d933be96ee" />

Line 3: Here we put 21 into the stack at bytes `0x1ff8`-`0x1ffb`

<img width="272" height="169" alt="image" src="https://github.com/user-attachments/assets/508841a1-ab54-44ac-94ca-304f982d003c" />

Line 4: we call the function `func`. This will push the return address (line 5) into the stack. We also move to line 7 to start the function code.

<img width="280" height="167" alt="image" src="https://github.com/user-attachments/assets/3d9573d7-f103-4618-ad41-01fa8c94d954" />

Line 7: here we start the function by reading the first parameter into register `rax` (from address `0xffc`). This does not modify the stack
Line 8: we read the second parameter into register `rcx` (from address `0xff8`). This does not modify the stack.
Line 9: we perform the add instruction between `rax` and `rcx` with the result being placed in `rax`. This does not modify the stack.
Line 10: we place the result from `rax` into the stack (address `0x1ff4`)

<img width="280" height="176" alt="image" src="https://github.com/user-attachments/assets/7c37530b-d344-4425-92bf-61433d71c743" />

Line 11: we return from the function. This pops the return address from the stack and moves to it (line 5)

<img width="286" height="182" alt="image" src="https://github.com/user-attachments/assets/7caf3a4b-c616-42d2-9ab7-6b449650fc3d" />

Line 5: we read the result from the stack, indicated by the stack pointer (address `0x1ff4`). This does not modify the stack.
Line 6: we return the stack pointer to its original position before our code.

<img width="254" height="157" alt="image" src="https://github.com/user-attachments/assets/b9df2f94-baf1-4480-90df-19290b955921" />

> [!NOTE]
> The example above is written in _NASM_ with _x86_ architecture and the intel syntax.
> The code also removes use of registers for parameter passing and returning to demonstrate stack-only use.

You can see that we finished with the stack returning to its original state. This is exactly as intended, as the stack is meant to provide a _temporary_ space for data. The next call to a function in `main` will lead to a similar process, only differeing because the process is different. 

All these stack manipulations are done automatically by the compiler, we don't actually have to handle it ourselves. This makes the stack very comfortable and safe to use.

You might also have noticed that the caller (`main`) and callee (`func`) have to coordinate where each thing should be placed (which addresses, in what order and so on). If `func` looked for the first parameter _after_ the second, then it would have the wrong value. This coordination is called _Application Binary Interface_ (_ABI_) and ensures that the way the code compiles is consistent in such a way that functions can work with each other.

> [!NOTE]
> If you ever heared the term _stack overflow_ or _stack overrun_, this usually refers to an issue where
> the program has reached the end of the stack. I.e. it has no more space in the stack and cannot continue.
> It could also refer to situations where code access the wrong locations on the stack.

For another example, I've set up a program with a debugger to show how the stack is managed for us
```java
public class Main {
    public static void main(String[] args) {
        int a = 55;
        int b = 21;

        int result = add(55, 21);
        System.out.println(result);
    }

    static int add(int num1, int num2) {
        int value1 = 4;
        value1 *= num1;

        if (value1 > num2) {
            int value2 = 5;
            value2 += num2;
            value1 -= value2;
        }

        return value1;
    }
}
```

<img width="419" height="488" alt="image" src="https://github.com/user-attachments/assets/45425598-629d-4d38-a101-2b08ef7122ce" />

Take a look at the list of variables below. These are the _local variables_, stored on the stack. We are now going to enter a new function. Notice what happens to these variables.

<img width="404" height="334" alt="image" src="https://github.com/user-attachments/assets/38b933b3-f988-439e-b0e2-5a1de3c5aa7f" />

Now we are shown different variables. This is because when we entered the new function, the debugger is showing us only the local variables of the current function call. So this new function starts with the parameters as variables. Let's move on.

<img width="390" height="335" alt="image" src="https://github.com/user-attachments/assets/584b97f7-5987-4fb9-b9b2-e58bb866a886" />

Now we have a new local variable, because it was just added to the stack. The stack code generated by the compiler freed extra space for it and set a value. Kinda like we did in our assembly code.

<img width="387" height="431" alt="image" src="https://github.com/user-attachments/assets/31fa8f07-b610-44fc-a92f-a573ab697b6e" />

As we enter the `if` we now get a new variable created on the stack, as the code wants.

<img width="386" height="453" alt="image" src="https://github.com/user-attachments/assets/4fa4375b-2b91-4912-8c88-fea8ef6f80bf" />

And when we exit this `if`, the variable is removed, because it is no longer used (out of scope) and the stack code just removes it for us. When we return from the function, the same will happen to the rest of the variables.

<img width="417" height="541" alt="image" src="https://github.com/user-attachments/assets/78f2c0c4-e4ac-44de-b793-4d78094c28b8" />

Basically, we don't need to manage the memory operations, the compiler will generate appropriate stack code to handle it for us. 

We can also see the _call stack_ from the debugger. Which originates from the stack itself, this is just parsed and named for human readibility.

<img width="315" height="280" alt="image" src="https://github.com/user-attachments/assets/f8472075-738b-47c7-b204-62f1f53d4578" />

#### Summary

We have seen a bit on how the stack operates and is used. It can be complicated behind the scenes, but because the compiler takes care of this for us, we do not have to work hard.

Basically, all local variables, and function calls rely on stack use. These are placed and removed from the stack as needed, with the stack pointer going back and forth consistently.

### Heap Memory

The heap space is a generic use memory for the program. It does not really have any specific uses in general, but it is rather up to the program to decide what to do with it. Because of that, its interface is rudementry: allocate and free. The user just requests the heap for memory, specifiying the wanted size and receives it. When done using it, the user notifies the heap that the memory can be freed to be used some other time.

#### Structure

Because the interface is so generic, the structure of an heap can vary a lot. Though there are very common patterns shared by typical implementations. But being software-defined and not hardware defined means that one can encounter many different implementations.

In general, the heap space is seen as a contiguous segment of memory, from which blocks can be extracted for the user. When memory is requested, a block of the wanted size is found, marked as _used_ and returned to the user. It must be guaranteed that this block will not be returned again until it is freed, otherwise two different codes might access the same memory without knowing. 

The user can do as they wish with this memory. The heap doesn't even need to know what it is used for, just that is is used. Eventually the user will finish using this memory and return it to the heap. The heap will mark the block as unused and might use it again in the future.

<img width="442" height="174" alt="image" src="https://github.com/user-attachments/assets/2ac3dfae-61ab-4d5c-931a-bfe984ed065e" />

For _Java_ specifically, the heap is implemented and managed by the _JVM_. Its structure is based on a binary tree and divides the heap into segments by the _age_ of the allocation. This may seem unusual, but it is mosly influenced by the fact that _Java_ allocates A LOT and the existence of the _Garbage Collector_ (explained later).

The following shows the segments used by the _HotSpot_ _JVM_ Heap

<img width="857" height="453" alt="image" src="https://github.com/user-attachments/assets/60b244b6-8c77-4690-9edb-d81dc6e6b418" />

You can see by the names of the segments that the category is based on _time_. Blocks are normally allocated in the _young generation_ and will be moved to the _old generation_ after enough time. This is of course a simplification.

#### Use

#### Garbage Collector

### Comparison

| Aspect | Stack | Heap |
|--------|-------|------|
| Runtime Cost | Low, fast to operate and use | High, allocation of data is costly |
| Coding cost | Low, code is generated by the compiler | Medium, programmer must explicitly use |
| Management | Simple, generated code takes care of allocation and freeing | Complex, programmer or JVM must take care of allocation, freeing and proper referencing |
| Space | Very Limited | Large and expandable |
| Defined By | CPU/Hardware | OS/Software |
| Lifecycle | Scope/Function | Undefined | 

## Code Memory
