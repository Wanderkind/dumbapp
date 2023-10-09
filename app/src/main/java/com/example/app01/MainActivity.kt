package com.example.app01

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.app01.ui.theme.App01Theme
import android.widget.Button
import android.widget.EditText
import android.widget.TextView


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val inputKey: EditText = findViewById(R.id.inputKey)
        val key = inputKey.text /* length between 6 and 100 */
        val keyLength = key.length
        val inputIntext: EditText = findViewById(R.id.inputIntext)
        val intext = inputIntext.text
        val intextLength = intext.length

        val outputButton1: Button = findViewById(R.id.Button1)
        val outputButton2: Button = findViewById(R.id.Button2)
        val outputTextView: TextView = findViewById(R.id.outputTextView)
        val resultsView: TextView = findViewById(R.id.results)

        outputButton1.setOnClickListener {
            val cipherText = encrypt(key.toString(), intext.toString())
            val ciphLength = cipherText.length
            val resultText =
                "\nPlaintext length = $intextLength\nKey length = $keyLength\nCiphertext length = $ciphLength"
            outputTextView.text = cipherText
            resultsView.text = resultText
        }

        outputButton2.setOnClickListener {
            val plainText = decrypt(key.toString(), intext.toString())
            val plainLength = plainText.length
            val resultText =
                "\nCiphertext length = $intextLength\nKey length = $keyLength\nPlaintext length = $plainLength"
            outputTextView.text = plainText
            resultsView.text = resultText
        }
    }
}

/*
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    App01Theme {
        Greeting("Android")
    }
}
*/


fun mod (a: Int, b: Int): Int {
    return (if (a >= 0) a % b else (b - (-a % b)) % b)
}

fun arrayArbitrary(arr: IntArray, p: Int): Int {
    val len = arr.size
    var ans = 0; val z = 10 % p; var k = 1
    for(i in 0 until len) {
        ans += arr[len - i - 1]*k
        k = (z*k) % p
    }
    return ans % p
}

fun arrayTwentyfour(arr: IntArray): Int {
    val len = arr.size
    var ans = 0
    for(i in 0 until len - 3) ans += arr[i]*16
    return (ans + arr[len - 3]*4 + arr[len - 2]*10 + arr[len - 1]) % 24
}

fun arrayTwentyfour(arr: Array<Int>): Int {
    val len = arr.size
    var ans = 0
    for(i in 0 until len - 3) ans += arr[i]*16
    return (ans + arr[len - 3]*4 + arr[len - 2]*10 + arr[len - 1]) % 24
}

fun modExp(n: Int, k: Int, p: Int): Int {
    var vn = n.toLong(); var vk = k; var x = 1L
    while(vk > 0) {
        x = if(vk % 2 == 1) (vn*x) % p else x
        vk /= 2
        vn = (vn*vn) % p
    }
    return x.toInt()
}

fun modExp4(n: Int, k: Int): Int {
    var vn = n.toLong(); var vk = k; var x = 1L
    while(vk > 0) {
        x = if(vk % 2 == 1) (vn*x) % 10000 else x
        vk /= 2
        vn = (vn*vn) % 10000
    }
    return x.toInt()
}

fun largeK(key: String): Array<IntArray> {

    val res = Array(5) { IntArray(24) }

    val v = key.length
    if (v < 6) throw Error("key should be at least 6 characters long")
    if (100 < v) throw Error("key should be no longer 100 characters")

    var d0 = 0; var d1 = 0; var d2 = 0; var d3 = 0; var d4 = 0
    var q0 = 0; var q1 = 0; var q2 = 0; var q3 = 0; var q4 = 0
    var b: Int
    for(u in 0 until v) {
        b = key[u].code
        d0 += b % 11383
        d1 += ((2*u + 1)*(b % 6257)) % 6257
        d2 += mod((1 - 2*(u % 2))*(b + u + 2)*(b % 7873), 7873)
        d3 += ((u + 3)*(b/(((13*b) % 11) % 5 + 2))) % 9811
        d4 += ((modExp(u, 3, 16369) + 6*u + b % 23 + 4)*(b % 16369)) % 16369
        q0 += b % 10000
        q1 += ((2*u + 1)*(b % 10000)) % 10000
        q2 += mod((1 - 2*(u % 2))*(b + u + 2)*(b % 10000), 10000)
        q3 += ((u + 3)*(b/(((13*b) % 11) % 5 + 2))) % 10000
        q4 += ((modExp(u, 3, 10000) + 6*u + b % 23 + 4)*(b % 10000)) % 10000
    }

    val dd0 = (857*d0) % 11383
    val dd1 = (179*d1) % 6257
    val dd2 = mod(113*d2, 7873)
    val dd3 = (163*d3) % 9811
    val dd4 = (419*d4) % 16369
    var tv: Int; var j: Int;  var k: Int;  var l: Int;  var kk: Int; var fk: Int
    var f0: Int; var f1: Int; var f2: Int; var f3: Int; var f4: Int

    for(t in 0 until 6) {
        tv = (t + v) % 10000
        j = key[t].code % 10000
        fk = key[tv - 6].code
        k = fk % 10000
        l = key[v/2 + t - 3].code % 10000
        kk = (fk - (k % 100))/100

        f0 = (
                modExp4(tv, 5) + modExp4(tv, 4) + 1012*modExp4(tv, 3) +
                        8658*modExp4(tv, 2) + 3429*tv + 199*((j*k) % 10000) + 1319*j +
                        393*modExp4(k, 2) + 5224*k + kk*(174 + kk) + 673*l + 3*modExp4(dd0, 2) +
                        modExp4(146, dd0) + (286*t + 8431 + q1)*t
                ) % 10000
        for(y in 0 until 4) {
            res[0][4*t + 3 - y] = f0 % 10
            f0 /= 10
        }

        f1 = (
                3*modExp4(tv, 5) + 8*modExp4(tv, 4) + 872*modExp4(tv, 3) +
                        1846*modExp4(tv, 2) + 2855*tv + 495*((j*k) % 10000) + 233*j +
                        8239*modExp4(k, 2) + 5108*k + kk*(370 + 667*kk) + 293*l + 5*modExp4(dd1, 2) +
                        modExp4(342, dd1) + (38*t + 509 + q2)*t
                ) % 10000
        for(y in 0 until 4) {
            res[1][4*t + 3 - y] = f1 % 10
            f1 /= 10
        }

        f2 = (
                modExp4(tv, 5) + 4*modExp4(tv, 4) + 1838*modExp4(tv, 3) +
                        5868*modExp4(tv, 2) + 3411*tv + 17*((j*k) % 10000) + 431*j +
                        771*modExp4(k, 2) + 8738*k + kk*(88 + 11*kk) + 1619*l + 11*modExp4(dd2, 2) +
                        modExp4(194, dd2) + (191*t + 902 + q3)*t
                ) % 10000
        for(y in 0 until 4) {
            res[2][4*t + 3 - y] = f2 % 10
            f2 /= 10
        }

        f3 = (
                3*modExp4(tv, 5) + 2*modExp4(tv, 4) + 1734*modExp4(tv, 3) +
                        4930*modExp4(tv, 2) + 487*tv + 271*((j*k) % 10000) + 2111*j +
                        1115*modExp4(k, 2) + 3732*k + kk*(68 + 83*kk) + 73*l + 7*modExp4(dd3, 2) +
                        modExp4(658, dd3) + (131*t + 221 + q4)*t
                ) % 10000
        for(y in 0 until 4) {
            res[3][4*t + 3 - y] = f3 % 10
            f3 /= 10
        }

        f4 = (
                2*modExp4(tv, 5) + 7*modExp4(tv, 4) + 3589*modExp4(tv, 3) +
                        8951*modExp4(tv, 2) + 248*tv + 63*((j*k) % 10000) + 4421*j +
                        1718*modExp4(k, 2) + 4487*k + kk*(97 + 56*kk) + 569*l + 12*modExp4(dd4, 2) +
                        modExp4(927, dd4) + (726*t + 1039 + q0)*t
                ) % 10000
        for(y in 0 until 4) {
            res[4][4*t + 3 - y] = f4 % 10
            f4 /= 10
        }
    }

    return res
}

val p = arrayOf(5,7,11,13,17,19,23,29,31,37,41,43,47,53,59,61,67,71,73,79,83,89,97,101)
val largeM = arrayOf(
    arrayOf(0,1,2,3), arrayOf(0,1,3,2), arrayOf(0,2,1,3),
    arrayOf(0,2,3,1), arrayOf(0,3,1,2), arrayOf(0,3,2,1),
    arrayOf(1,0,2,3), arrayOf(1,0,3,2), arrayOf(1,2,0,3),
    arrayOf(1,2,3,0), arrayOf(1,3,0,2), arrayOf(1,3,2,0),
    arrayOf(2,0,1,3), arrayOf(2,0,3,1), arrayOf(2,1,0,3),
    arrayOf(2,1,3,0), arrayOf(2,3,0,1), arrayOf(2,3,1,0),
    arrayOf(3,0,1,2), arrayOf(3,0,2,1), arrayOf(3,1,0,2),
    arrayOf(3,1,2,0), arrayOf(3,2,0,1), arrayOf(3,2,1,0)
)
val largeMr = arrayOf(
    arrayOf(0,1,2,3), arrayOf(0,1,3,2), arrayOf(0,2,1,3),
    arrayOf(0,3,1,2), arrayOf(0,2,3,1), arrayOf(0,3,2,1),
    arrayOf(1,0,2,3), arrayOf(1,0,3,2), arrayOf(2,0,1,3),
    arrayOf(3,0,1,2), arrayOf(2,0,3,1), arrayOf(3,0,2,1),
    arrayOf(1,2,0,3), arrayOf(1,3,0,2), arrayOf(2,1,0,3),
    arrayOf(3,1,0,2), arrayOf(2,3,0,1), arrayOf(3,2,0,1),
    arrayOf(1,2,3,0), arrayOf(1,3,2,0), arrayOf(2,1,3,0),
    arrayOf(3,1,2,0), arrayOf(2,3,1,0), arrayOf(3,2,1,0)
)

fun encrypt(key: String, textc: String): String {
    val v = key.length
    val lK = largeK(key)
    fun largeD(m: Int): IntArray {
        val ix = lK[m % 5][m] + lK[(2*m + 2) % 5][23 - m]
        return lK[ix % 5]
    }

    val klkl = ArrayList<ArrayList<Int>>()
    var kn: ArrayList<Int>; var tk: Int; var k2: Int
    var kk2: Int; var f2: Int; var y: Int; var fk: Int
    for(i in 0 until 24) {
        kn = ArrayList()
        for(t in 0 until p[i]) {
            tk = t + largeD(0)[i]
            fk = key[mod(p[i] - t - 1, v)].code
            k2 = fk % 10000
            kk2 = (fk - (k2 % 100))/100
            f2 = mod(
                modExp4(tk, 5) + modExp4(tk, 4) +
                        (971 + largeD(1)[i])*modExp4(tk, 3) +
                        (6947 - largeD(2)[i])*modExp4(tk, 2) +
                        (2279 + largeD(3)[i])*tk +
                        (282 - largeD(4)[i])*modExp4(k2, 2) +
                        (4103 + largeD(5)[i])*k2 + modExp4(kk2, 2) +
                        (194 - largeD(6)[i])*kk2 + 3411*i + 841*t*i + 7407*t + 5988
                , 10000)
            y = largeM[(11*i + (i + 7)*t + 5*largeD(7)[i]) % 24][largeD(8)[i] % 4]
            for(z in 0 until 3 - y) f2 /= 10
            kn += f2 % 10
        }
        klkl += kn
    }

    val nb = textc.length
    if(nb > 0) {
        var ot: Int
        for (x in 0 until nb) {
            ot = textc[x].code
            if (196607 < ot) throw Error("Unicode value for every character in text should be no greater than 196607")
        }
    }

    val concat = String(intArrayOf(0), 0, 1).single()
    val text = if (nb % 2 == 1) textc + concat else textc
    val n = if (nb % 2 == 1) nb + 1 else nb

    val lxx = ArrayList<Int>()
    for(i in 0 until 6*n/12) {
        for(j in 0 until 24) {
            lxx += (largeD(9)[j]*p[1 + i % 23] + i*(22*i + 13) + 17*j) % 10
        }
    }

    val largeG = 2*6*n
    for(i in 0 until largeG) {
        for(u in 0 until 4) {
            lxx[i] = (lxx[i] + lxx[(i + p[20 + u]) % (6*n/12)]) % 10
            lxx[i] = (lxx[mod(11527*(i - arrayTwentyfour(largeD(10)))
                    + (14029/(u + 1)), largeG)] + i*(i % 10 + 2)*(u + 3)) % 10
        }
    }

    var xx: Int; val ww = IntArray(4)
    val cq = ArrayList<Int>()
    for(i in 0 until 6*n/4) {
        xx = (largeD(11)[8]*10 + largeD(12)[19] + 5*i) % 16
        for(j in 0 until 4) {
            ww[3 - j] = xx % 2
            xx /= 2
        }
        cq += (arrayTwentyfour(arrayOf(
            lxx[4*i + ww[0]], lxx[4*i + ww[1] + 1], lxx[4*i + ww[2] + 2], lxx[4*i + ww[3] + 3])
        ) + arrayArbitrary(largeD(13), (27*i + 7)%(p[i%24]*p[(i+11)%24]) + 3)) % 24
    }

    var a1 = ""; var st1: Long
    var kv: Int; var yy: String
    var z: Int; val skb = IntArray(47) {0}; val sko = IntArray(17) {0}
    val k3 = key[v - 1].code.toLong(); st1 = k3*(k3*k3 + 2L)
    var st2: Long = st1
    var ind = 46
    while(st1 > 0L) {
        skb[ind] = (st1 % 2L).toInt()
        st1 /= 2L
        ind--
    }
    ind = 16
    while(st2 > 0L) {
        sko[ind] = (st2 % 8L).toInt()
        st2 /= 8L
        ind--
    }
    for(x in 0 until n) {
        z = text[x].code
        if (z == 0) z = 201546
        kv = key[x % v].code
        y =  ( 902708
                - 15394*((8*x) % 11)
                - 9705*(6 - (5*modExp(x, 2, 7)) % 7)
                - 7817*(12 - ((4*x) % 13))
                - 4211*((3*modExp(kv, 2, 16) + 5*x) % 16)
                + 2981*((kv % 100) % 5)
                - 2692*(22 - (6*modExp(x, 2, 23) % 23))
                + 179*(2*modExp(kv, 3, 37) % 37)
                - 41*(886 - (x % 887))
                + 17*mod(modExp(x, 4, 643) - 19*modExp(x, 3, 643) + 270*x, 643)
                - z*(skb[46 - (x % 47)] + 1)
                + 129*skb[(6*x) % 47]
                + 301*(sko[16 - ((2*x) % 17)] + 2) )
        yy = y.toString()
        a1 += yy
    }

    val rw = a1.toList()
    val rx = IntArray(6*n); val r = IntArray(6*n)
    val klf = klkl[0]
    val kint = klf[0]*10000 + klf[1]*1000 + klf[2]*100 + klf[3]*10 + klf[4]

    for(i in 0 until 6*n) rx[i] = rw[i].code - 48
    for(i in 0 until 6*n) r[i] = rx[(i + kint) % (6*n)]

    for(j in 0 until 7) {
        for(i in 0 until 6*n) {
            r[i] = (r[i] + r[mod(i - j - 1, 6*n)]) % 10
            r[i] = (r[i] + r[mod(i + j - p[23 - j] + 1, 6*n)]) % 10
        }
    }

    var e: Int
    for(i in 0 until 24) {
        e = i % 6
        if(e < 3) {
            for(j in 0 until 6*n) r[mod(17*i + j, 6*n)] =
                mod(r[mod(17*i + j, 6*n)] + (e - 3)*klkl[i][j % p[i]], 10)
        } else {
            for(j in 0 until 6*n) r[mod(17*i - j, 6*n)] =
                mod(r[mod(17*i - j, 6*n)] + (e - 2)*klkl[i][j % p[i]], 10)
        }
    }

    var c = ""; val r4 = IntArray(4); var irs: Int; var g: Int
    for(i in 0 until 6*n/4) {
        for(w in 0 until 4) r4[w] = r[4*i + largeM[cq[i]][w]]
        irs = 1000*r4[0] + 100*r4[1] + 10*r4[2] + r4[3]
        g = arrayArbitrary(largeD((2*i) % 24) + largeD((2*i + 1) % 24), 11172)
        c += if(g < 10000) {
            if(irs < g) (44032 + irs).toChar()
            else (45204 + irs).toChar()
        } else (34032 + g + (2*irs) % 10001).toChar()
    }

    if (c.length != 3*n/2) throw Error("Error")
    return c
}

fun decrypt(key: String, lC: String): String {
    val v = key.length
    val lK = largeK(key)
    fun largeD(m: Int): IntArray {
        val ix = lK[m % 5][m] + lK[(2*m + 2) % 5][23 - m]
        return lK[ix % 5]
    }

    val klkl = ArrayList<ArrayList<Int>>()
    var kn: ArrayList<Int>; var tk: Int; var k2: Int
    var kk2: Int; var f2: Int; var yy: Int; var fk: Int
    for(i in 0 until 24) {
        kn = ArrayList()
        for(t in 0 until p[i]) {
            tk = t + largeD(0)[i]
            fk = key[mod(p[i] - t - 1, v)].code
            k2 = fk % 10000
            kk2 = (fk - (k2 % 100))/100
            f2 = mod(
                modExp4(tk, 5) + modExp4(tk, 4) +
                        (971 + largeD(1)[i])*modExp4(tk, 3) +
                        (6947 - largeD(2)[i])*modExp4(tk, 2) +
                        (2279 + largeD(3)[i])*tk +
                        (282 - largeD(4)[i])*modExp4(k2, 2) +
                        (4103 + largeD(5)[i])*k2 + modExp4(kk2, 2) +
                        (194 - largeD(6)[i])*kk2 + 3411*i + 841*t*i + 7407*t + 5988
                , 10000)
            yy = largeM[(11*i + (i + 7)*t + 5*largeD(7)[i]) % 24][largeD(8)[i] % 4]
            for(z in 0 until 3 - yy) f2 /= 10
            kn += f2 % 10
        }
        klkl += kn
    }

    val lc = lC.length
    if(lc % 3 != 0) {
        throw Error("Ciphertext invalid")
    }
    if(lc > 0) {
        var ot: Int
        for (x in 0 until lc) {
            ot = lC[x].code
            if (ot < 44032 || 55203 < ot) throw Error("Includes invalid character.")
        }
    }

    val lxx = ArrayList<Int>()
    for(i in 0 until lc/3) {
        for(j in 0 until 24) {
            lxx += (largeD(9)[j]*p[1 + i % 23] + i*(22*i + 13) + 17*j) % 10
        }
    }

    val largeG = 8*lc
    for(i in 0 until largeG) {
        for(u in 0 until 4) {
            lxx[i] = (lxx[i] + lxx[(i + p[20 + u]) % (4*lc/12)]) % 10
            lxx[i] = (lxx[mod(11527*(i - arrayTwentyfour(largeD(10)))
                    + (14029/(u + 1)), largeG)] + i*(i % 10 + 2)*(u + 3)) % 10
        }
    }

    var xx: Int; val ww = IntArray(4)
    val cq = ArrayList<Int>()
    for(i in 0 until lc) {
        xx = (largeD(11)[8]*10 + largeD(12)[19] + 5*i) % 16
        for(j in 0 until 4) {
            ww[3 - j] = xx % 2
            xx /= 2
        }
        cq += (arrayTwentyfour(arrayOf(
            lxx[4*i + ww[0]], lxx[4*i + ww[1] + 1], lxx[4*i + ww[2] + 2], lxx[4*i + ww[3] + 3])
        ) + arrayArbitrary(largeD(13), (27*i + 7)%(p[i%24]*p[(i+11)%24]) + 3)) % 24
    }

    val rq = IntArray(lc*4)
    var o: Int; var g: Int; var irsb: Int; var tmp: Int
    for(i in 0 until lc) {
        o = lC[i].code
        g = arrayArbitrary(largeD((2*i) % 24) + largeD((2*i + 1) % 24), 11172)
        irsb = if( g < 10000) {
            o - (if(o - g < 44032) 44032 else 45204)
        } else {
            (o - g - (if(o % 2 == g % 2) 34032 else 24031))/2
        }
        for(w in 0 until 4) {
            tmp = irsb
            repeat(3 - largeMr[cq[i]][w]) {tmp /= 10}
            rq[4*i + w] = tmp % 10
        }
    }
    val lr = rq.size
    var i: Int; var e: Int
    for(h in 0 until 24) {
        i = 23 - h
        e = i % 6
        if(e < 3) {
            for(j in 0 until lr) rq[mod(17*i + j, lr)] =
                mod(rq[mod(17*i + j, lr)] - (e - 3)*klkl[i][j % p[i]], 10)
        } else {
            for(j in 0 until lr) rq[mod(17*i - j, lr)] =
                mod(rq[mod(17*i - j, lr)] - (e - 2)*klkl[i][j % p[i]], 10)
        }
    }

    var j: Int
    for(h in 0 until 7) {
        j = 6 - h
        for(b in 0 until lr) {
            i = lr - 1 - b
            rq[i] = mod(rq[i] - rq[mod(i + j - p[23 - j] + 1, lr)], 10)
            rq[i] = mod(rq[i] - rq[mod(i - j - 1, lr)], 10)
        }
    }

    val klf = klkl[0]
    val kint = klf[0]*10000 + klf[1]*1000 + klf[2]*100 + klf[3]*10 + klf[4]
    val rx = IntArray(lr)
    for(w in 0 until lr) rx[w] = rq[mod(w - kint, lr)]
    var p = ""; var st1: Long
    val k3 = key[v - 1].code.toLong(); st1 = k3*(k3*k3 + 2L)
    var st2: Long = st1; var kv: Int
    val skb = IntArray(47) {0}; val sko = IntArray(17) {0}
    var ind = 46; var zz: Int
    while(st1 > 0L) {
        skb[ind] = (st1 % 2L).toInt()
        st1 /= 2L
        ind--
    }
    ind = 16
    while(st2 > 0L) {
        sko[ind] = (st2 % 8L).toInt()
        st2 /= 8L
        ind--
    }

    var y: Int
    for(x in 0 until lr/6) {
        y = 0
        for(h in 0 until 6) {
            y += rx[6*x + h]
            y *= 10
        }
        y /= 10
        kv = key[x % v].code
        zz = ( 902708
                - 15394*((8*x)%11)
                - 9705*(6 - ((5*modExp(x, 2, 7))%7))
                - 7817*(12 - ((4*x)%13))
                - 4211*(((3*modExp(kv, 2, 16)) + 5*x)%16)
                + 2981*((kv%100)%5)
                - 2692*(22 - ((6*modExp(x, 2, 23))%23))
                + 179*((2*modExp(kv, 3, 37))%37)
                - 41*(886 - (x%887))
                + 17*mod(modExp(x, 4, 643) - 19*modExp(x, 3, 643) + 270*x, 643)
                + 129*(skb[(6*x)%47])
                + 301*(sko[16 - ((2*x)%17)] + 2)
                - y) / (skb[46 - (x % 47)] + 1)
        if (zz < 0) throw Error("Error")
        if (zz in 1..196606) p += zz.toChar()
    }

    return p
}