from storage import usl

if __name__ == "__main__":
    key = usl.create("dfba")
    f = usl.get(key)
    if f:
        print f
        f.write("hello")
        f.close()
    usl.delete(key)
