## Subaddressed replies (follow-up of [an old experiment](https://github.com/Zyaen/thunderbird-android/tree/fix/reply))

This is a follow-up of an experiment from about a year ago (before their refactor). I picked it up again and re-did it recently.

### What it does (why it exists)

If you’re using “name + subaddress” style addresses (for example `name+tag@example.com`), it’s not really right that the reply ends up using only the *name/pretty* part. The app should reply using the *exact address they actually wrote to* (including the subaddress part).

### Defaults / behavior details

- If the delimiter is missing or empty, it falls back to `+`.

