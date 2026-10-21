# CP7 - Burn-rate alert policy

Alerts of `06-alerts.yml` protect SLO 1 of `05-slo.md`: 99.5 % availability of
`POST /api/v1/sagas/place-order` in `opti-workflow`, 30-day window (720 hours), error budget 0.5 %
= 6 failed requests = 216 minutes of full outage.

Burn rate 1 spends the whole budget in exactly 30 days. Budget spent by an alert before it
fires = burn rate x window / 720 hours.

## How much budget each alert means

| Alert | Burn rate | Long window | Short window (confirms it) | Threshold on the error ratio | Budget spent before it fires | In requests | In outage minutes | Budget gone in |
|---|---|---|---|---|---|---|---|---|
| `PlaceOrderFastBurn` (page) | 14.4 | 1 h | 5 min | 14.4 x 0.005 = 7.2 % | 14.4 x 1 / 720 = **2 %** | 0.12 of 6 | 4.3 of 216 | 50 hours |
| `PlaceOrderSlowBurn` (ticket) | 6 | 6 h | 30 min | 6 x 0.005 = 3 % | 6 x 6 / 720 = **5 %** | 0.30 of 6 | 10.8 of 216 | 5 days |

The short window stops the alert from staying active after the problem is gone: the long window
alone would still show a high ratio for an hour after recovery.

## Why there is a minimum of requests

The shop sells about 40 times a day, around 4 requests per hour. With so few events one failed
request out of two would be a 50 % ratio. The rules add `increase(...) >= 3` in the last hour for
the page and `>= 10` in the last 6 hours for the ticket, so a quiet night does not wake anyone.
With 3 or more requests in the hour, one failure already fires the page. That is deliberate for
now: a single failure is one sixth (17 %) of the monthly budget. The thresholds are reviewed once
real traffic is measured (`05-slo.md`, traffic estimate).

## Two cause-based alerts we will NOT create

1. **CPU of the `opti-workflow` container above 80 %.** The saga mostly waits for HTTP answers
   from `opti-customers-api`, `opti-products-api` and `opti-sales-api`, so CPU says little about
   the seller. It can be high with every order succeeding, and it can be low while every call
   times out. The burn-rate alert fires only when users are hurt.
2. **One `participant.call.timeout` log line (or one retry) toward `opti-products-api`.** Bounded
   retries with back-off are part of the design of `ParticipantClient`; a retry that works costs
   the seller a second, not an order. Paging for each timeout would teach the team to ignore the
   pager. If timeouts become frequent they show up in the latency SLO (`05-slo.md`, SLO 2) and in
   the fast-burn alert.
