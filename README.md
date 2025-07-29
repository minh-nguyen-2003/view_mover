# ViewMover – Android Library

**ViewMover** giúp bạn dễ dàng **di chuyển một View** (ImageView, Button, GIF, v.v.) **từ một View nguồn (A) sang View đích (B)**, với khả năng tùy chỉnh **điểm neo (anchor)** và **điểm gắn (attach)**.

---

## ⚙️ Cài đặt (qua JitPack)

Thêm vào build.gradle (project):

```
	dependencyResolutionManagement {
		repositoriesMode.set(RepositoriesMode.FAIL-ON-PROJECT-REPOS)
		repositories {
			mavenCentral()
			maven { url = uri("https://jitpack.io") }
		}
	}
```

Thêm dependency vào build.gradle (module):

```
	dependencies {
	        implementation("com.github.minh-nguyen-2003:view-mover:1.0.1")
	}
```
## ⚠️ Nhớ thay 1.0.0 bằng tag release phù hợp.
## 💡 Cách sử dụng cơ bản
```kotlin
  ViewMover
    .move(binding.gifView)
    .to(binding.targetView)
    .start()
```

## 🏷 Hàm khởi tạo & cài đặt
| Hàm | Ý nghĩa |
|----------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------|
| move(viewMove: View) | Khởi tạo một ViewMover mới với view cần di chuyển (viewMove). |
| from(viewA: View) | Chỉ định View xuất phát (nếu không dùng, mặc định xuất phát từ vị trí hiện tại). |
| to(viewB: View) | Chỉ định View đích (bắt buộc). |
| anchor(selfPoint: Point) | Chỉ định điểm neo trên viewMove (mặc định là CENTER). |
| attachTo(targetPoint: Point) | Chỉ định điểm gắn trên View đích (mặc định là CENTER). |
| duration(duration: Long) | Thời gian animation (ms). |

## Hàm bắt đầu di chuyển
| Hàm | Ý nghĩa |
|----------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------|
| start() | Đi từ A đến B.<br>• Nếu viewMove đang nằm trên A → di chuyển từ vị trí hiện tại.<br>• Nếu không nằm trên A → nhảy về A rồi di chuyển đến B.<br>• Nếu không có A → di chuyển từ vị trí hiện tại. |
| startSmooth() | Luôn di chuyển từ vị trí hiện tại của viewMove đến View B (bỏ qua from()). |

## Điều khiển animation
| Hàm | Ý nghĩa |
|----------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------|
| pause() | Tạm dừng animation (API ≥ 19). Với Android thấp hơn sẽ tự động cancel(). |
| resume() | Tiếp tục animation sau khi pause() (API ≥ 19). |

## Enum Point
ViewMover hỗ trợ 9 vị trí neo/gắn:

| Enum | Mô tả |
|----------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------|
| CENTER | Chính giữa |
| TOP | Mép trên, giữa |
| BOTTOM | Mép dưới, giữa |
| LEFT | Mép trái, giữa |
| RIGHT | Mép phải, giữa |
| TOP-LEFT | Góc trên trái |
| TOP-RIGHT | Góc trên phải |
| BOTTOM-LEFT | Góc dưới trái |
| BOTTOM-RIGHT | Góc dưới phải |

```kotlin
  .anchor(ViewMover.Point.TOP-LEFT)      // Neo góc trên trái của viewMove
  .attachTo(ViewMover.Point.CENTER)      // Gắn vào tâm của view đích
```

## Ví dụ hoàn chỉnh
```kotlin
  binding.btnA.setOnClickListener {
      ViewMover
          .move(binding.gifView)
          .from(binding.btnA)                      // Bắt đầu từ btnA
          .to(binding.btnB)                        // Kết thúc ở btnB
          .anchor(ViewMover.Point.TOP-LEFT)        // Neo góc trên trái gifView
          .attachTo(ViewMover.Point.CENTER)        // Gắn vào tâm của btnB
          .duration(1500)                          // 1.5 giây
          .start()
  }
  
  binding.btnB.setOnClickListener {
      ViewMover
          .move(binding.gifView)
          .to(binding.btnC)                        // Không có from() → bay từ vị trí hiện tại
          .startSmooth()
  }

```

## License
```
IT License

Copyright (c) 2025 Nguyễn Đức Minh

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

```
