import sys
import chardet

def detect_charset(file_path):
    with open(file_path, 'rb') as f:
        raw_data = f.read()
        result = chardet.detect(raw_data)
        return result['encoding']

if __name__ == "__main__":
    if len(sys.argv) != 2:
        print("Usage: python detect_charset.py <file_path>")
        sys.exit(1)

    file_path = sys.argv[1]
    charset = detect_charset(file_path)
    print(charset)