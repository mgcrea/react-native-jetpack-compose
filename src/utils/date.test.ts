import { describe, expect, it } from "@jest/globals";

import { fromEpochMillis, fromMinutesFromMidnight, toEpochMillis, toMinutesFromMidnight } from "./date";

describe("toEpochMillis", () => {
  it("returns undefined for nullish input", () => {
    expect(toEpochMillis(undefined)).toBeUndefined();
    expect(toEpochMillis(null)).toBeUndefined();
  });

  it("passes numbers through", () => {
    expect(toEpochMillis(0)).toBe(0);
    expect(toEpochMillis(1704067200000)).toBe(1704067200000);
  });

  it("parses ISO strings", () => {
    expect(toEpochMillis("2024-01-01T00:00:00.000Z")).toBe(1704067200000);
  });

  it("converts Date objects", () => {
    const date = new Date("2024-01-01T00:00:00.000Z");
    expect(toEpochMillis(date)).toBe(date.getTime());
  });

  it("returns undefined instead of NaN for unparseable input", () => {
    expect(toEpochMillis("not a date")).toBeUndefined();
    expect(toEpochMillis(new Date("garbage"))).toBeUndefined();
    expect(toEpochMillis(Number.NaN)).toBeUndefined();
  });
});

describe("fromEpochMillis", () => {
  it("returns null for nullish input", () => {
    expect(fromEpochMillis(undefined)).toBeNull();
    expect(fromEpochMillis(null)).toBeNull();
  });

  it("treats epoch 0 as a valid date", () => {
    expect(fromEpochMillis(0)).toEqual(new Date(0));
  });

  it("converts millis to a Date", () => {
    expect(fromEpochMillis(1704067200000)).toEqual(new Date(1704067200000));
  });
});

describe("toMinutesFromMidnight", () => {
  it("returns undefined for nullish input", () => {
    expect(toMinutesFromMidnight(undefined)).toBeUndefined();
    expect(toMinutesFromMidnight(null)).toBeUndefined();
  });

  it("converts local hours and minutes", () => {
    expect(toMinutesFromMidnight(new Date(2024, 0, 1, 14, 30))).toBe(870);
    expect(toMinutesFromMidnight(new Date(2024, 0, 1, 0, 0))).toBe(0);
    expect(toMinutesFromMidnight(new Date(2024, 0, 1, 23, 59))).toBe(1439);
  });
});

describe("fromMinutesFromMidnight", () => {
  it("returns null for nullish input", () => {
    expect(fromMinutesFromMidnight(undefined)).toBeNull();
    expect(fromMinutesFromMidnight(null)).toBeNull();
  });

  it("builds a Date on the base day with seconds and millis zeroed", () => {
    const base = new Date(2024, 5, 15, 8, 45, 12, 345);
    const result = fromMinutesFromMidnight(870, base);
    expect(result?.getFullYear()).toBe(2024);
    expect(result?.getMonth()).toBe(5);
    expect(result?.getDate()).toBe(15);
    expect(result?.getHours()).toBe(14);
    expect(result?.getMinutes()).toBe(30);
    expect(result?.getSeconds()).toBe(0);
    expect(result?.getMilliseconds()).toBe(0);
  });

  it("does not mutate the base date", () => {
    const base = new Date(2024, 5, 15, 8, 45);
    const snapshot = base.getTime();
    fromMinutesFromMidnight(870, base);
    expect(base.getTime()).toBe(snapshot);
  });

  it("defaults to today", () => {
    const result = fromMinutesFromMidnight(0);
    expect(result?.toDateString()).toBe(new Date().toDateString());
  });
});
