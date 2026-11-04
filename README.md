# pondo-api

Score backend for [pondo.games](https://pondo.games), a site with free daily
puzzle games (Slice and Downstream).

When a player finishes a daily puzzle, the site sends their score here. The
API stores it and returns how everyone else did on that puzzle, which the
site uses to show "you beat X% of players" and a score chart.

## Stack

- Java 21, Spring Boot
- PostgreSQL, with Flyway migrations
- Caffeine for caching, Bucket4j for rate limiting
- Docker Compose, behind Nginx on a DigitalOcean droplet

## Endpoints

| Method | Path | What it does |
|---|---|---|
| `POST` | `/api/v1/scores` | Submit a finished puzzle and get the score distribution back |
| `GET` | `/api/v1/scores?game=slice&puzzle=14` | Get the score distribution for a puzzle |
| `GET` | `/actuator/health` | Health check |

Both score endpoints return the distribution of total scores plus the
distribution for each round.

## How it works

- **One score per player per puzzle.** Each submission carries a random ID
  made by the site for that puzzle. Resubmitting is safe: duplicates are
  ignored and the distribution is still returned.
- **Validation.** Scores are checked against each game's rules (number of
  rounds, points per round, total matches the rounds) before being saved.
- **Caching.** Distributions are cached for a short time, so heavy traffic
  doesn't mean heavy database load.
- **Rate limiting.** Requests are limited per IP.
- **No personal data.** No accounts and no names. Just scores, the
  per-puzzle ID and a timestamp.
