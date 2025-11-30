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



## Data Memory

Data memory, which is memory used to store information, not code, is generally divided into two distinct structures: _The Heap_ and _The Stack_.
Each of these was made for a very specific set of uses, meaning that a program will need to use both and can rarely rely on just one of them.

Most program data memory is both readable and writable, meaning my program can read the information in the memory and write information into the memory. But not all memory
is such, some, typically _global const_ memory is typically only readable, but this is not of interest to us at the moment.

### Stack Memory

The _stack_ memory, named so after its data structure - a stack, is used to store temporary local information as well as method call information (leading to it sometimes called _call stack_).

<img width="276" height="342" alt="image" src="https://github.com/user-attachments/assets/8b21ecbb-1963-4e46-85cb-852a4adb4d48" />

#### Structure

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

  sub rsp, 12                      # clear space for the 2 parameters + return value
  mov dword [rsp + 8], 55          # put 55 at the first parameter location
  mov dword [rsp + 4], 21          # put 21 at the second parameter location
  call func                        # calls the function, will place the return address on the stack
  mov rax, dword [rsp]             # function will return to here, read the return value from the stack
  add rsp, 12                      # restore the stack pointer to before the call

  ... # the code after

func:
  mov rax, dword [rsp + 8 + 4 + 4] # read the first parameter into rax.
                                   # Need to skip the return address, the second parameter and the return value location

  mov rcx, dword [rsp + 8 + 4]     # read the first parameter into rcx.
                                   # Need to skip the return address and the return value location

  add rax, rcx                     # add rax and rcx. result will be in rax

  mov dword [rsp + 8], rax         # write the result from rax into the location on stack for the return value
  ret                              # return to the return address on stack (location rsp)
```

> [!NOTE]
> The example above is written in _NASM_ with _x86_ architecture and the intel syntax.
> The code also removes use of registers for parameter passing and returning to demonstrate stack-only use.

### Heap Memory

## Code Memory
