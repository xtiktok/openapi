package hmac
 
import (
	"crypto/hmac"
	"crypto/sha256"
        "encoding/hex"
)
 
func Encode(str string,key []byte) string {
	mac := hmac.New(sha256.New, key)
	mac.Write([]byte(str))
	return  hex.EncodeToString(mac.Sum(nil))
}
