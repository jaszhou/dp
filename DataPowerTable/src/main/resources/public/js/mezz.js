if (!window.AWSC) {
	window.AWSC = {}
}
window.AWSC.stdeltaStartTime = new Date().getTime();
if (!window.AWSC) {
	var AWSC = {}
}
AWSC.Clog = (function() {
	var u = 100;
	var i = [], U = function() {
	}, O = [], e = [], j = [], x = "", a, W, F = false;
	var w = false, I = false, T = false;
	var h = function l(ag, aa, af, ad, ac, Z, ab) {
		var ae = {}, Y = K();
		if (typeof ag === "object") {
			ae = ag
		} else {
			ae.key = ag;
			if (aa !== undefined) {
				ae.value = aa
			}
			if (af !== undefined) {
				ae.detail = af
			}
			if (ac) {
				ae.unit = ac
			}
			if (Z) {
				ae.metricType = Z
			}
			if (ab) {
				ae.logLevel = ab
			}
		}
		if (ae.pageId) {
			ad = ae.pageId
		}
		if (!ad) {
			ad = W
		}
		ae.pageId = k(ad, ae.metricType);
		switch (ae.logLevel) {
		case "INFO":
			h("INFO", 1, undefined, ae.pageId);
			if (!w) {
				w = true;
				h("custInfo", 1, undefined, ae.pageId)
			}
			break;
		case "WARN":
			h("WARN", 1, undefined, ae.pageId);
			if (!I) {
				I = true;
				h("custWarn", 1, undefined, ae.pageId)
			}
			break;
		case "FATAL":
			h("FATAL", 1, undefined, ae.pageId);
			if (!T) {
				T = true;
				h("custFatal", 1, undefined, ae.pageId)
			}
		}
		if (ae.key) {
			if (Y && Y.key === ae.key && Y.detail === ae.detail
					&& Y.pageId === ae.pageId && Y.unit === ae.unit
					&& ae.value === 1) {
				Y.value += 1
			} else {
				if (i.length < u) {
					i.push(ae)
				}
			}
			if (U) {
				U()
			}
		} else {
		}
	}, s = function(ac, ab, Z, Y) {
		var aa = i.length;
		h(ac, ab, Z, Y);
		if (i.length > aa) {
			i.unshift(i.pop())
		}
	}, z = function C() {
		return i.shift()
	}, R = function V() {
		return i[0]
	}, K = function g() {
		return i[i.length - 1]
	}, X = function n(Y) {
		U = Y
	}, E = function S(Y) {
		W = Y
	}, m = function() {
		var ac = location.pathname.replace(/\//g, "_"), ab = window.location.hash, Y, aa;
		if (ac[0] == "_") {
			ac = ac.substr(1)
		}
		if (!ac) {
			return "_"
		} else {
			var Z = ac.indexOf("_");
			ac = ac.substring(Z + 1)
		}
		Y = ab.match(/^#?s=(.+)/);
		if (Y) {
			aa = Y[1];
			if (aa == "Home") {
				aa = ""
			}
		} else {
			Y = ab.match(/^#?([^:]+):.*/);
			if (Y) {
				aa = Y[1]
			}
		}
		if (aa) {
			ac += "_" + aa
		}
		return ac
	}, k = function D(Z, aa) {
		var Y;
		if (typeof Z === "function") {
			Y = Z(aa)
		} else {
			Y = Z
		}
		if (aa && !Y) {
			Y = m()
		}
		if (Y) {
			Y = Y.replace(/[^a-zA-Z0-9_]/g, "")
		}
		return Y
	}, p = function H(aa, Z, Y) {
		h("jsError", aa, Z, Y);
		if (!F) {
			h("custJsError", 1);
			F = true
		}
	}, L = function y(aa, Z, Y) {
		p(1, "MSG:" + aa + " URL:" + Z + " LN:" + Y)
	}, B = function M(Y) {
		if (Y.message) {
			p(1, Y.message)
		} else {
			p(1, Y.toString())
		}
	}, q = function d(Y) {
		x = Y
	}, r = function t(Y) {
		a = Y
	}, o = function A() {
		if (a) {
			return a
		}
		if (x) {
			return x
		}
		return ""
	};
	h("clogPing", 1);
	window.onerror = function Q(ab, Z, Y) {
		L(ab, Z, Y);
		for (var aa = 0; aa < O.length; aa++) {
			O[aa](ab, Z, Y)
		}
		return false
	};
	window.onerror.sbh = true;
	var P = function f(Y) {
		O.push(Y)
	};
	window.error = function G(Y) {
		B(Y);
		for (var Z = 0; Z < e.length; Z++) {
			e[Z](Y)
		}
		return false
	};
	window.error.sbh = true;
	var J = function b(Y) {
		e.push(Y)
	};
	window.onbeforeunload = function N(Z) {
		U(true);
		var ac, Y, ab;
		for (var aa = 0; aa < j.length; aa++) {
			ac = j[aa](Z);
			Y = (typeof Z.returnValue === "string" && Z.returnValue !== "") ? Z.returnValue
					: undefined;
			if (ac === undefined && Y) {
				ac = Y
			}
			if (ac !== undefined) {
				ab = ac
			}
		}
		if (ab !== undefined) {
			Z.returnValue = ab
		}
		return ab
	};
	window.onbeforeunload.sbh = true;
	var v = function c(Y) {
		j.push(Y)
	};
	return {
		log : h,
		addOnErrorHandler : P,
		addErrorHandler : J,
		addOnBeforeUnloadHandler : v,
		setDefaultEndpoint : q,
		setEndpointOverride : r,
		setPageId : E,
		system : {
			getEndpoint : o,
			dequeue : z,
			curItem : R,
			onEnqueue : X,
			prequeue : s
		}
	}
})();
var AWSCLog = AWSC.Clog;
if (window.AWSConsoleMetrics && window.AWSConsoleMetrics.pageId) {
	AWSC.Clog.setPageId(window.AWSConsoleMetrics.pageId)
}
(function() {
	var a = document.getElementById("awsc-alternate-host");
	if (a) {
		AWSC.Clog.setEndpointOverride("https://" + a.content)
	}
})();
(function() {
	var b = document.getElementById("awsc-mezz-fb"), a = 1;
	if (b) {
		a = (b.content === "0" ? 0 : 1)
	}
	AWSC.Clog.log("navFb", a)
})();
if (!window.AWSC) {
	var AWSC = {}
}
if (typeof XMLHttpRequest == "undefined") {
	XMLHttpRequest = function() {
		try {
			return new ActiveXObject("Msxml2.XMLHTTP.6.0")
		} catch (a) {
		}
		try {
			return new ActiveXObject("Msxml2.XMLHTTP.3.0")
		} catch (a) {
		}
		try {
			return new ActiveXObject("Microsoft.XMLHTTP")
		} catch (a) {
		}
		throw new Error("This browser does not support XMLHttpRequest.")
	}
}
AWSC.CustomEvents = {
	customListeners : {},
	resetCustomListeners : function(a) {
		if (a) {
			this.customListeners[a] = []
		} else {
			this.customListeners = {}
		}
	},
	getCustomListeners : function(a) {
		var b = this.customListeners[a];
		if (!b) {
			b = [];
			this.customListeners[a] = b
		}
		return b
	},
	addCustomListener : function(a, b) {
		this.getCustomListeners(a).push(b)
	},
	fireCustomEvent : function(b, a) {
		var d = this.getCustomListeners(b);
		for (var c = 0; c < d.length; c += 1) {
			try {
				d[c].apply(null, a)
			} catch (f) {
			}
		}
	}
};
AWSC.XhrEvents = {
	addBeforeXhrOpenListener : function(a) {
		AWSC.CustomEvents.addCustomListener("beforexhropen", a)
	},
	addAfterXhrDoneListener : function(a) {
		AWSC.CustomEvents.addCustomListener("afterxhrdone", a)
	},
	fireBeforeXhrOpen : function(a) {
		AWSC.CustomEvents.fireCustomEvent("beforexhropen", arguments)
	},
	fireAfterXhrDone : function() {
		AWSC.CustomEvents.fireCustomEvent("afterxhrdone", arguments)
	},
	resetXhrListeners : function() {
		AWSC.CustomEvents.resetCustomListeners("beforexhropen");
		AWSC.CustomEvents.resetCustomListeners("afterxhrdone")
	},
	init : function() {
		var a = XMLHttpRequest.prototype;
		if (!a) {
			return
		}
		if (a.extendxhr) {
			return
		}
		a.extendxhr = true;
		a.callAfterDone = function() {
			if (this.readyState === 4) {
				AWSXhrEvents.fireAfterXhrDone(this)
			}
		};
		a.originalOpen = a.open;
		a.open = function() {
			var b = this;
			var c = Array.prototype.slice.apply(arguments);
			b.urlcalled = c[1];
			AWSC.XhrEvents.fireBeforeXhrOpen(b);
			if (b.addEventListener) {
				b.addEventListener("readystatechange", a.callAfterDone, false)
			} else {
				if (window.attachEvent) {
					b.oldOnreadystatechange = b.onreadystatechange;
					b.onreadystatechange = function() {
						if (b.oldOnreadystatechange) {
							var d = Array.prototype.slice.apply(arguments);
							b.oldOnreadystatechange.apply(b, c)
						}
						b.callAfterDone()
					}
				}
			}
			a.originalOpen.apply(b, c)
		}
	}
};
AWSC.XhrEvents.init();
var AWSCustomEvents = AWSC.CustomEvents;
var AWSXhrEvents = AWSC.XhrEvents;
if (!window.AWSC) {
	AWSC = {}
}
AWSC.Timer = (function() {
	var B = {}, r = window.AWSC.XhrEvents, b = {}, f = true, c = 0, E = 0, w = 100, z = false, F, n, v = {}, G = {}, k = {}, H = function() {
	}, l;
	if (window.performance) {
		l = window.performance.timing
	} else {
		l = {}
	}
	var u = {
		navigationStart : l.navigationStart,
		unloadEventStart : l.unloadEventStart,
		unloadEventEnd : l.unloadEventEnd,
		redirectStart : l.redirectStart,
		redirectEnd : l.redirectEnd,
		fetchStart : l.fetchStart,
		domainLookupStart : l.domainLookupStart,
		domainLookupEnd : l.domainLookupEnd,
		connectStart : l.connectStart,
		connectEnd : l.connectEnd,
		secureConnectionStart : l.secureConnectionStart,
		requestStart : l.requestStart,
		responseStart : l.responseStart,
		responseEnd : l.responseEnd,
		domLoading : l.domLoading,
		domInteractive : l.domInteractive,
		domContentLoadedEventStart : l.domContentLoadedEventStart,
		domContentLoadedEventEnd : l.domContentLoadedEventEnd,
		domComplete : l.domComplete,
		loadEventStart : l.loadEventStart,
		loadEventEnd : l.loadEventEnd,
		redirectCount : l.redirectCount
	};
	if (window.AWSC.Clog && AWSC.Clog.log) {
		F = AWSC.Clog.log
	} else {
		F = H
	}
	var C = Date.now ? Date.now : function() {
		return new Date().getTime()
	};
	if (window.performance) {
		B.clickTime = window.performance.timing.navigationStart;
		B.startTime = window.performance.timing.responseStart
	} else {
		if (window.AWSConsoleMetrics) {
			B = window.AWSConsoleMetrics
		}
	}
	var t = 1406876400000;
	if (!B.startTime || B.startTime < t) {
		B.startTime = C();
		B.clickTime = 0;
		n = 1
	} else {
		n = 0
	}
	F("fst", n);
	AWSC.startTime = B.startTime;
	AWSC.clickTime = B.clickTime;
	var g = function(O, M, N) {
		F(M, N, "", "", "ms", O)
	};
	var o = function(M, N) {
		g("pageload", M, N - B.startTime)
	};
	o("initialLoad", C());
	var I = function(M, N) {
		if (B.clickTime != 0) {
			g("pageload", M, N - B.clickTime)
		}
	};
	var J = function(M, N) {
		g("custom", M, N)
	};
	var d = function(M) {
		b[M] = C()
	};
	var s = function(M) {
		var O = C(), N = b[M];
		if (N) {
			delete b[M];
			J(M, O - N)
		}
	};
	var x = function(M) {
		delete b[M]
	};
	var e = function(M, N) {
		J(M, parseInt(N, 10))
	};
	var L = function(M) {
		if (typeof M !== "object") {
			throw new Error("Invalid argument.")
		}
		if (!M.hasOwnProperty("metricName")) {
			throw new Error("Must have a metricName in the argument object")
		}
		var N = M.metricName;
		if (!M.hasOwnProperty("events") || (M.events.length < 1)) {
			throw new Error(
					'setMetricEvents requires at least one event in "events" array')
		}
		var P = M.events;
		if (N in G) {
			throw new Error(N
					+ " events may not be set more than once per page load")
		}
		k[N] = false;
		if (M.startTime === "click") {
			if (B.clickTime !== 0) {
				k[N] = true
			} else {
				delete k[N];
				return
			}
		}
		G[N] = true;
		v[N] = {};
		for (var O = 0; O < P.length; O++) {
			v[N][P[O]] = false
		}
	};
	var y = function(M) {
		if (v.hasOwnProperty(M)) {
			for ( var N in v[M]) {
				if (v[M][N] === false) {
					return false
				}
			}
			return true
		}
	};
	var a = function(M) {
		var O = C();
		if (M && v) {
			for ( var N in v) {
				if (v[N].hasOwnProperty(M)) {
					if (v[N][M] === false) {
						v[N][M] = true;
						if (y(N)) {
							if (k[N]) {
								I(N, O)
							} else {
								o(N, O)
							}
							if (N === "customerReady") {
								j()
							}
							delete v[N];
							delete k[N]
						}
					}
				}
			}
		}
	};
	var K = function(N, M) {
		L({
			metricName : "customerReady",
			events : N
		});
		L({
			metricName : "clickToCustomerReady",
			startTime : "click",
			events : N
		})
	};
	var p = function(M) {
		h(M)
	};
	var h = function(M) {
		if (M) {
			a(M)
		} else {
			var N = C();
			o("customerReady", N);
			I("clickToCustomerReady", N);
			j()
		}
	};
	var j = function() {
		h = H
	};
	var D = function(N) {
		if (!N.urlcalled) {
			return false
		}
		var M = /feedback\/custsat\/1\/popquestion/;
		return M.test(N.urlcalled)
	};
	var i = function(M) {
		if (!D(M)) {
			c++;
			if (f) {
				f = false;
				o("pageReady", C())
			}
		}
	};
	var m = function(N) {
		if (!D(N)) {
			var M;
			c--;
			if (c === 0) {
				M = C();
				E = M;
				setTimeout(function() {
					q(M)
				}, w)
			}
		}
	};
	var q = function(M) {
		if (c === 0 && M === E && !z) {
			g("pageload", "loadFinished", E - B.startTime);
			A();
			z = true
		}
	};
	var A = function A() {
		if (!window.performance) {
			return
		}
		var N = l.navigationStart;
		for ( var M in u) {
			if (u.hasOwnProperty(M)) {
				if (u[M]) {
					F(M, u[M] - N, "", "", "ms")
				} else {
					setTimeout(function() {
						if (u[M]) {
							F(M, u[M] - N, "", "", "ms")
						}
					}, 2000)
				}
			}
		}
	};
	r.addBeforeXhrOpenListener(i);
	r.addAfterXhrDoneListener(m);
	return {
		start : d,
		stop : s,
		submitCustomTimer : e,
		discardTimer : x,
		isInitialLoadPending : false,
		metricEvent : a,
		setMetricEvents : L,
		customerReady : p,
		setCustomerReadyEvents : K,
		initialLoadFlag : false
	}
})();
var metricsTimer = AWSC.Timer;
(function() {
	if (AWSC.XhrEvents && AWSC.Clog) {
		var a = function a(c) {
			c.startTime = new Date().getTime()
		}, b = function b(d) {
			var c = (new Date().getTime()) - d.startTime;
			AWSC.Clog.log({
				key : "ajaxStatus" + d.status,
				value : c,
				unit : "ms"
			})
		};
		AWSC.XhrEvents.addBeforeXhrOpenListener(a);
		AWSC.XhrEvents.addAfterXhrDoneListener(b)
	}
})();