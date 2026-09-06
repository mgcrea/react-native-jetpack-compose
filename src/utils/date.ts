/**
 * A date value accepted by the picker components: a `Date`, epoch milliseconds, or an ISO string.
 */
export type DateInput = Date | number | string;

/**
 * Converts a Date, number (epoch millis), or ISO string to epoch milliseconds.
 * Returns `undefined` for nullish or unparseable input so nothing invalid reaches native.
 */
export function toEpochMillis(value: DateInput | null | undefined): number | undefined {
  if (value == null) return undefined;
  const millis = typeof value === "number" ? value : new Date(value).getTime();
  return Number.isNaN(millis) ? undefined : millis;
}

/**
 * Converts epoch milliseconds received from native back to a Date.
 * Epoch 0 is a valid instant and yields `new Date(0)`; only nullish input yields `null`.
 */
export function fromEpochMillis(millis: number | null | undefined): Date | null {
  return millis == null ? null : new Date(millis);
}

/**
 * Converts a Date to minutes from midnight (0-1439) in local time.
 */
export function toMinutesFromMidnight(time: Date | null | undefined): number | undefined {
  if (time == null) return undefined;
  return time.getHours() * 60 + time.getMinutes();
}

/**
 * Creates a Date on the given base day (defaults to today) at the given minutes from midnight.
 * Pass the same `base` for both ends of a range so they land on the same calendar day.
 */
export function fromMinutesFromMidnight(
  minutes: number | null | undefined,
  base: Date = new Date(),
): Date | null {
  if (minutes == null) return null;
  return new Date(
    base.getFullYear(),
    base.getMonth(),
    base.getDate(),
    Math.floor(minutes / 60),
    minutes % 60,
    0,
    0,
  );
}
